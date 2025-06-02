package ci.komobe.actionelle.infrastructure.rest.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Value("${server.url}")
  private String serverUrl;

  @Value("${server.port}")
  private int serverPort;

  @Bean
  public OpenAPI customOpenAPI() {
    String url = String.format("%s:%d", serverUrl, serverPort);

    SecurityScheme securityScheme = new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .description("Entrez votre token JWT ici");

    return new OpenAPI()
        .info(new Info()
            .title("Action'Elles API")
            .version("1.0")
            .description("API de gestion des souscriptions d'assurance pour Action'Elles")
            .contact(new Contact()
                .name("Moro KONÉ")
                .email("moro.kone@komobe.ci")))
        .addServersItem(new Server()
            .url(url)
            .description("Local ENV"))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new Components()
            .addSecuritySchemes("bearerAuth", securityScheme));
  }
}