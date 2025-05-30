package ci.komobe.actionelle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "ci.komobe.actionelle.infrastructure.configs")
public class ActionelleApplication {

  public static void main(String[] args) {
    SpringApplication.run(ActionelleApplication.class, args);
  }
}
