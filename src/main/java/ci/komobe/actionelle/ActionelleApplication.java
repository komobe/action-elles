package ci.komobe.actionelle;

import ci.komobe.actionelle.domain.valueobjects.Role;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.UserJpaEntity;
import ci.komobe.actionelle.infrastructure.hibernatejpa.repositories.UserJpaRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "ci.komobe.actionelle.infrastructure.configs")
public class ActionelleApplication {

  public static void main(String[] args) {
    SpringApplication.run(ActionelleApplication.class, args);
  }


  @Bean
  @Profile("data-test")
  @SuppressWarnings("java:S6437")
  public CommandLineRunner insertDataTest(
      UserJpaRepository repository, PasswordEncoder passwordEncoder) {
    return args -> {
      if (repository.findByUsername("adminUser").isEmpty()) {
        var admin = new UserJpaEntity();
        admin.setId(UUID.randomUUID().toString());
        admin.setUsername("adminUser");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRole(Role.ADMIN);
        repository.save(admin);
      }

      if (repository.findByUsername("amazone").isEmpty()) {
        var admin = new UserJpaEntity();
        admin.setId(UUID.randomUUID().toString());
        admin.setUsername("amazoneUser");
        admin.setPassword(passwordEncoder.encode("amazone"));
        admin.setRole(Role.AMAZONE);
        repository.save(admin);
      }
    };
  }


  @Bean
  @Profile("data-test")
  @SuppressWarnings("java:S106")
  public CommandLineRunner generateKey() {
    return args -> {
      // Génère une clé sécurisée pour HS512
      var key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
      String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

      System.out.println("=== CLÉ JWT SÉCURISÉE GÉNÉRÉE ===");
      System.out.println("Copiez cette valeur dans votre application.properties :");
      System.out.println("jwt.secret=" + encodedKey);
      System.out.println("\nTaille de la clé: " + key.getEncoded().length * 8 + " bits");
    };
  }
}
