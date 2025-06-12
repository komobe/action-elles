package ci.komobe.actionelle;

import ci.komobe.actionelle.infrastructure.security.properties.CorsProperties;
import ci.komobe.actionelle.infrastructure.security.properties.RateLimitProperties;
import ci.komobe.actionelle.infrastructure.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Point d'entrée de l'application
 * 
 * @author Moro KONÉ 2025-06-03
 */
@Slf4j
@SpringBootApplication
@EnableConfigurationProperties({ RateLimitProperties.class, SecurityProperties.class, CorsProperties.class })
public class ActionelleApplication {
  public static void main(String[] args) {
    SpringApplication.run(ActionelleApplication.class, args);
  }
}
