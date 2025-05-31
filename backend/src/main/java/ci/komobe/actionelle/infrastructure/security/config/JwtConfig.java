package ci.komobe.actionelle.infrastructure.security.config;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Configuration
public class JwtConfig {

  @Value("${jwt.secret}")
  private String secret;
  @Getter
  @Value("${jwt.expiration:86400000}")
  private long expiration;

  @Bean
  public Key jwtSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }
}