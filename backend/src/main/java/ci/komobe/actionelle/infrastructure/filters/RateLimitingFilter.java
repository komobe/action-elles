package ci.komobe.actionelle.infrastructure.filters;

import ci.komobe.actionelle.infrastructure.configs.properties.RateLimitProperties;
import io.github.bucket4j.BandwidthBuilder.BandwidthBuilderBuildStage;
import io.github.bucket4j.BandwidthBuilder.BandwidthBuilderCapacityStage;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

  private final RateLimitProperties rateLimit;

  private final Map<String, Bucket> bucketMap = new ConcurrentHashMap<>();

  public RateLimitingFilter(RateLimitProperties rateLimit) {
    this.rateLimit = rateLimit;
  }

  private Function<BandwidthBuilderCapacityStage, BandwidthBuilderBuildStage> configureRateLimiting() {
    return limit -> limit
        .capacity(rateLimit.getMaxRequests())
        .refillGreedy(rateLimit.getMaxRequests(), rateLimit.getByDuration());
  }

  private Bucket createNewBucket() {
    return Bucket.builder().addLimit(configureRateLimiting()).build();
  }

  private String getClientIpAddress(HttpServletRequest request) {
    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
    if (xForwardedForHeader == null) {
      return request.getRemoteAddr();
    } else {
      // X-Forwarded-For peut contenir plusieurs IPs, prendre la première
      return xForwardedForHeader.split(",")[0].trim();
    }
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {

    if (rateLimit.isEnabled()) {
      filterChain.doFilter(request, response);
    }

    String ip = getClientIpAddress(request);
    Bucket bucket = bucketMap.computeIfAbsent(ip, k -> createNewBucket());

    if (bucket.tryConsume(1)) {
      filterChain.doFilter(request, response);
    } else {
      response.setStatus(429);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setHeader("Retry-After", "60");
      response.getWriter().write(
          "{\"error\":\"Rate limit exceeded\",\"message\":\"Too many requests. Try again in 60 seconds.\"}");
    }
  }
}