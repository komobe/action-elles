package ci.komobe.actionelle.infrastructure.security.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

  private List<String> whiteList = new ArrayList<>();
  private Map<String, RoleConfig> roles = new HashMap<>();
  private DefaultServerConfig defaultServerConfig = new DefaultServerConfig();
  private Map<String, ServerConfig> servers = new HashMap<>();

  @Getter
  @Setter
  public static class DefaultServerConfig {
    private List<String> allowedMethods = new ArrayList<>();
    private List<String> allowedHeaders = new ArrayList<>();
    private List<String> exposedHeaders = new ArrayList<>();
    private Long maxAge;
  }

  @Getter
  @Setter
  public static class RoleConfig {
    private Map<String, List<String>> urls = new HashMap<>();
  }

  @Getter
  @Setter
  public static class ServerConfig {
    private String origin;
    private String role;
    private List<String> allowedMethods;
    private List<String> allowedHeaders;
    private List<String> exposedHeaders;
    private Long maxAge;
    private boolean enabled = false;

    public void applyDefaults(DefaultServerConfig defaults) {
      if (allowedMethods == null || allowedMethods.isEmpty()) {
        allowedMethods = new ArrayList<>(defaults.getAllowedMethods());
      }
      if (allowedHeaders == null || allowedHeaders.isEmpty()) {
        allowedHeaders = new ArrayList<>(defaults.getAllowedHeaders());
      }
      if (exposedHeaders == null || exposedHeaders.isEmpty()) {
        exposedHeaders = new ArrayList<>(defaults.getExposedHeaders());
      }
      if (maxAge == null) {
        maxAge = defaults.getMaxAge();
      }
    }
  }

  public Map<String, List<String>> getUrlsForServer(String serverName) {
    ServerConfig serverConfig = servers.get(serverName);
    if (serverConfig == null || !serverConfig.isEnabled()) {
      return new HashMap<>();
    }
    return roles.getOrDefault(serverConfig.getRole(), new RoleConfig()).getUrls();
  }
}