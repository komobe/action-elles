package ci.komobe.actionelle.seeders;

import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.repositories.GarantieRepository;
import ci.komobe.actionelle.domain.valueobjects.Prime;
import ci.komobe.actionelle.domain.valueobjects.PuissanceFiscale;
import ci.komobe.actionelle.domain.valueobjects.TypeBaseCalcul;
import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeder pour les garanties
 * 
 * @author Moro KONÉ 2025-06-03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GarantiesSeeder {

  private final GarantieRepository repository;

  @Transactional
  public void seed() {
    log.info("Seeding des garanties...");

    List<Garantie> garanties = List.of(
        // RC
        Garantie.builder()
            .code("RC")
            .libelle("Responsabilité Civile")
            .description("RC 2 CV")
            .puissanceFiscale(PuissanceFiscale.fromDebut(2))
            .baseDeCalcul(TypeBaseCalcul.PUISSANCE_FISCALE)
            .prime(new Prime(TypeMontantPrime.MONTANT, BigDecimal.valueOf(37601)))
            .maxAge(-1)
            .plafonne(false)
            .build(),
        Garantie.builder()
            .code("RC")
            .libelle("Responsabilité Civile")
            .description("RC 3-6 CV")
            .puissanceFiscale(PuissanceFiscale.of(3, 6))
            .baseDeCalcul(TypeBaseCalcul.PUISSANCE_FISCALE)
            .prime(new Prime(TypeMontantPrime.MONTANT, BigDecimal.valueOf(45181)))
            .maxAge(-1)
            .plafonne(false)
            .build(),
        Garantie.builder()
            .code("RC")
            .libelle("Responsabilité Civile")
            .description("RC 7-10 CV")
            .puissanceFiscale(PuissanceFiscale.of(7, 10))
            .baseDeCalcul(TypeBaseCalcul.PUISSANCE_FISCALE)
            .prime(new Prime(TypeMontantPrime.MONTANT, BigDecimal.valueOf(51078)))
            .maxAge(-1)
            .plafonne(false)
            .build(),
        Garantie.builder()
            .code("RC")
            .libelle("Responsabilité Civile")
            .description("RC 11-14 CV")
            .puissanceFiscale(PuissanceFiscale.of(11, 14))
            .baseDeCalcul(TypeBaseCalcul.PUISSANCE_FISCALE)
            .prime(new Prime(TypeMontantPrime.MONTANT, BigDecimal.valueOf(65677)))
            .maxAge(-1)
            .plafonne(false)
            .build(),
        Garantie.builder()
            .code("RC")
            .libelle("Responsabilité Civile")
            .description("RC 15-23 CV")
            .puissanceFiscale(PuissanceFiscale.of(15, 23))
            .baseDeCalcul(TypeBaseCalcul.PUISSANCE_FISCALE)
            .prime(new Prime(TypeMontantPrime.MONTANT, BigDecimal.valueOf(86456)))
            .maxAge(-1)
            .plafonne(false)
            .build(),
        Garantie.builder()
            .code("RC")
            .libelle("Responsabilité Civile")
            .description("RC > 24 CV")
            .puissanceFiscale(PuissanceFiscale.fromFin(24))
            .baseDeCalcul(TypeBaseCalcul.PUISSANCE_FISCALE)
            .prime(new Prime(TypeMontantPrime.MONTANT, BigDecimal.valueOf(104143)))
            .maxAge(-1)
            .plafonne(false)
            .build(),
        // Autres garanties
        Garantie.builder()
            .code("DOMMAGE")
            .libelle("Dommages")
            .description("Dommages 0-5 ans")
            .baseDeCalcul(TypeBaseCalcul.VALEUR_A_NEUF)
            .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(2.60)))
            .maxAge(5)
            .plafonne(false)
            .build(),
        Garantie.builder()
            .code("TIERCE_COLLISION")
            .libelle("Tierce Collision")
            .description("Tierce Collision 0-8 ans")
            .baseDeCalcul(TypeBaseCalcul.VALEUR_A_NEUF)
            .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(1.65)))
            .maxAge(8)
            .plafonne(false)
            .build(),
        Garantie.builder()
            .code("TIERCE_PLAFONNEE")
            .libelle("Tierce Plafonnée")
            .description("Tierce Plafonnée 0-10 ans")
            .baseDeCalcul(TypeBaseCalcul.VALEUR_ASSUREE)
            .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(4.20)))
            .primeMinimum(new Prime(TypeMontantPrime.MONTANT, BigDecimal.valueOf(100000)))
            .maxAge(10)
            .plafonne(true)
            .build(),
        Garantie.builder()
            .code("VOL")
            .libelle("Vol")
            .description("Garantie vol")
            .baseDeCalcul(TypeBaseCalcul.VALEUR_VENALE)
            .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(0.14)))
            .maxAge(-1)
            .plafonne(false)
            .build(),
        Garantie.builder()
            .code("INCENDIE")
            .libelle("Incendie")
            .description("Garantie incendie")
            .baseDeCalcul(TypeBaseCalcul.VALEUR_VENALE)
            .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(0.15)))
            .maxAge(-1)
            .plafonne(false)
            .build());

    for (var garantie : garanties) {
      repository.enregistrer(garantie);
    }

    log.info("{} garanties traitées", garanties.size());
  }
}