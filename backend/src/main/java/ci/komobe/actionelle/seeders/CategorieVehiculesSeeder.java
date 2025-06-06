package ci.komobe.actionelle.seeders;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Seeder pour les catégories de véhicules
 * 
 * @author Moro KONÉ 2025-06-03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CategorieVehiculesSeeder {

  private final CategorieVehiculeRepository repository;

  @Transactional
  public void seed() {
    if (!repository.lister().isEmpty()) {
      return;
    }

    log.info("Seeding des catégories de véhicules...");

    List<CategorieVehicule> categories = List.of(
        CategorieVehicule.builder()
            .id(IdGenerator.generateId())
            .code("201")
            .libelle("Promenade et Affaire")
            .description("Usage personnel")
            .build(),

        CategorieVehicule.builder()
            .id(IdGenerator.generateId())
            .code("202")
            .libelle("Véhicules Motorisés à 2 ou 3 roues")
            .description("Motocycle, tricycles")
            .build(),

        CategorieVehicule.builder()
            .id(IdGenerator.generateId())
            .code("203")
            .libelle("Transport public de voyage")
            .description("Véhicule transport de personnes")
            .build(),

        CategorieVehicule.builder()
            .id(IdGenerator.generateId())
            .code("204")
            .libelle("Véhicule de transport avec taximètres")
            .description("Taxis")
            .build());

    categories.forEach(categorie -> {
      if (!repository.existeParCode(categorie.getCode())) {
        repository.enregistrer(categorie);
        log.info("Catégorie créée : {}", categorie.getCode());
      }
    });

    log.info("{} catégories de véhicules traitées", categories.size());
  }
}