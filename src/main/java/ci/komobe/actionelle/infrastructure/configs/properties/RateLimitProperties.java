package ci.komobe.actionelle.infrastructure.configs.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties {

  private boolean enabled = true;

  @Min(1)
  private long maxRequests;

  @NotNull
  private Duration byDuration;

  public boolean isDisabled() {
    return !enabled;
  }
}