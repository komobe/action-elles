package ci.komobe.actionelle.infrastructure.config;

import ci.komobe.actionelle.domain.repositories.AssureRepository;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.GarantieRepository;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.seeders.AssureSeeder;
import ci.komobe.actionelle.seeders.CategorieVehiculeSeeder;
import ci.komobe.actionelle.seeders.DatabaseSeeder;
import ci.komobe.actionelle.seeders.GarantieSeeder;
import ci.komobe.actionelle.seeders.ProduitSeeder;
import ci.komobe.actionelle.seeders.UtilisateurSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration pour les seeders
 * 
 * @author Moro KONÉ 2025-06-03
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SeederConfig {

  private final Environment environment;
  private final AssureRepository assureRepository;
  private final GarantieRepository garantieRepository;
  private final ProduitRepository produitRepository;
  private final UtilisateurRepository utilisateurRepository;
  private final PasswordEncoder passwordEncoder;
  private final CategorieVehiculeRepository categorieVehiculeRepository;

  @Bean
  ApplicationRunner seedRunner() {
    return args -> {

      if (args.containsOption("seeds")) {
        if (isProdProfile()) {
          log.error("Les seeds sont interdits en production !");
          throw new UnsupportedOperationException("Les seeds sont interdits en production !");
        }

        String seedValue = args.getOptionValues("seeds").get(0);
        log.info("Valeur de l'option 'seeds' : {}", seedValue);

        if (Boolean.parseBoolean(seedValue)) {
          log.info("Démarrage du seeding...");

          // Création manuelle des seeders
          var assuresSeeder = new AssureSeeder(assureRepository);
          var categorieVehiculesSeeder = new CategorieVehiculeSeeder(categorieVehiculeRepository);
          var garantiesSeeder = new GarantieSeeder(garantieRepository);
          var produitsSeeder = new ProduitSeeder(produitRepository, garantieRepository, categorieVehiculeRepository);
          var utilisateursSeeder = new UtilisateurSeeder(utilisateurRepository, passwordEncoder);

          // Création du DatabaseSeeder
          var databaseSeeder = new DatabaseSeeder(
              utilisateursSeeder,
              categorieVehiculesSeeder,
              garantiesSeeder,
              produitsSeeder,
              assuresSeeder);

          databaseSeeder.run();
        } else {
          log.info("Seeding désactivé");
        }
      } else {
        log.info("Option 'seeds' non trouvée");
      }
    };
  }

  private boolean isProdProfile() {
    String[] activeProfiles = environment.getActiveProfiles();
    for (String profile : activeProfiles) {
      if (profile.equals("prod")) {
        return true;
      }
    }
    return false;
  }
}