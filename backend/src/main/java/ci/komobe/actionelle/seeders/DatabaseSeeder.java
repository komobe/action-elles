package ci.komobe.actionelle.seeders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeder principal qui coordonne l'exécution de tous les seeders
 * 
 * @author Moro KONÉ 2025-06-03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder {

  private final UtilisateursSeeder utilisateursSeeder;
  private final CategorieVehiculesSeeder categorieVehiculesSeeder;
  private final GarantiesSeeder garantiesSeeder;
  private final ProduitsSeeder produitsSeeder;
  private final AssuresSeeder assuresSeeder;

  /**
   * Exécute tous les seeders dans l'ordre des dépendances
   */
  @Transactional
  public void run() {
    log.info("Démarrage du seeding de la base de données...");

    // 1. Données de référence
    categorieVehiculesSeeder.seed();
    garantiesSeeder.seed();

    // 2. Produits (dépend des catégories et garanties)
    produitsSeeder.seed();

    // 3. Utilisateurs et Assurés (indépendants)
    utilisateursSeeder.seed();
    assuresSeeder.seed();

    log.info("Seeding terminé avec succès.");
  }
}