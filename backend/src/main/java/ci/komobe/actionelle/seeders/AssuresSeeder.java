package ci.komobe.actionelle.seeders;

import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Seeder pour les assurés
 * 
 * @author Moro KONÉ 2025-06-03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AssuresSeeder {

  private final AssureRepository repository;

  @Transactional
  public void seed() {
    if (!repository.lister().isEmpty()) {
      return;
    }

    log.info("Seeding des assurés...");

    List<Assure> assures = List.of(
        // Assuré test 1
        Assure.builder()
            .id(IdGenerator.generateId())
            .nom("SERY")
            .prenoms("Serge")
            .email("serge.sery@actionelle.ci")
            .telephone("+225 07 07 07 07 07")
            .numeroCarteIdentite("CI1234567890")
            .adresse("Bouaké, Air-France")
            .sexe("M")
            .dateNaissance(LocalDate.of(1990, 1, 1))
            .lieuNaissance("Abidjan")
            .build(),

        // Assuré test 2
        Assure.builder()
            .id(IdGenerator.generateId())
            .nom("TOURE")
            .prenoms("Aminata")
            .email("aminata.toure@actionelle.ci")
            .telephone("+225 01 01 01 01 01")
            .numeroCarteIdentite("CI0987654321")
            .adresse("Abidjan, Marcory")
            .sexe("F")
            .dateNaissance(LocalDate.of(1985, 6, 15))
            .lieuNaissance("Bouaké")
            .build(),

        // Assuré test 3
        Assure.builder()
            .id(IdGenerator.generateId())
            .nom("KOUASSI")
            .prenoms("Jean")
            .email("jean.kouassi@actionelle.ci")
            .telephone("+225 05 05 05 05 05")
            .numeroCarteIdentite("CI5678901234")
            .adresse("Abidjan, Yopougon")
            .sexe("M")
            .dateNaissance(LocalDate.of(1995, 12, 31))
            .lieuNaissance("Yamoussoukro")
            .build(),

        // Assuré test 4
        Assure.builder()
            .id(IdGenerator.generateId())
            .nom("KOUASSI")
            .prenoms("Jean")
            .email("jean.kouassi@actionelle.ci")
            .telephone("+225 05 05 05 05 05")
            .numeroCarteIdentite("CI7890123456")
            .adresse("Abidjan, Yopougon")
            .sexe("M")
            .dateNaissance(LocalDate.of(1995, 12, 31))
            .lieuNaissance("Yamoussoukro")
            .build());

    assures.forEach(assure -> {
      if (!repository.existeParEmail(assure.getEmail())) {
        repository.enregistrer(assure);
        log.info("Assuré créé : {}", assure.getEmail());
      }
    });

    log.info("{} assurés traités", assures.size());
  }
}