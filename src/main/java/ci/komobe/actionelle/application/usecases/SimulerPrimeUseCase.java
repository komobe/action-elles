package ci.komobe.actionelle.application.usecases;

import ci.komobe.actionelle.application.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.application.exceptions.ProduitError;
import ci.komobe.actionelle.application.presenters.SimulerPrimePresenter;
import ci.komobe.actionelle.application.repositories.ProduitRepository;
import ci.komobe.actionelle.application.services.prime.PrimeCalculator;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.entities.Produit;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

/**
 * UseCase pour simuler la prime d'un produit d'assurance
 *
 * @param produitRepository le repository des produits
 * @param primeCalculator   le calculateur de prime
 * @author Moro KONÉ 2025-05-29
 */
public record SimulerPrimeUseCase(
    ProduitRepository produitRepository,
    PrimeCalculator primeCalculator) {

  private static final double TAUX_CALCUL_VALEUR_ASSUREE = 0.5;

  public void execute(SimulerPrimeCommand command, SimulerPrimePresenter<?> presenter) {
    Produit produit = getProduit(command);
    double age = calculAge(command);

    BigDecimal totauxPrime = BigDecimal.ZERO;

    Garantie garantieRc = produit.getGarantiesResponsabiliteCivile().stream()
        .filter(garantie -> !garantie.estSuperieurMaxAge(age))
        .filter(garantie -> garantie.getPuissanceFiscale().isInRange(command.puissanceFiscale()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException(
            "Aucune garantie RC ne correspond à la puissance fiscale " + command.puissanceFiscale()
                + " et à l'âge "
                + age));

    BigDecimal primeRc = primeCalculator.calculer(garantieRc, command);
    totauxPrime = totauxPrime.add(primeRc);

    for (var garantie : produit.getAutresGaranties()) {
      if (garantie.estSuperieurMaxAge(age)) {
        continue;
      }

      BigDecimal montant = primeCalculator.calculer(garantie, command);

      if (garantie.isPlafonne() && BigDecimal.ZERO.compareTo(montant) < 0) {
        var valeurAssuree = command.valeurVenale()
            .multiply(BigDecimal.valueOf(TAUX_CALCUL_VALEUR_ASSUREE));

        if (montant.compareTo(valeurAssuree) >= 0) {
          montant = valeurAssuree;
        }
      }

      totauxPrime = totauxPrime.add(montant);
    }

    presenter.addData(totauxPrime);
  }

  private double calculAge(SimulerPrimeCommand command) {
    Period period = Period.between(command.dateDeMiseEnCirculation(), LocalDate.now());
    int years = period.getYears();
    int months = period.getMonths();
    int days = period.getDays();

    return years + (months / 12.0) + (days / 365.0);
  }

  private Produit getProduit(SimulerPrimeCommand command) {
    Produit produit = produitRepository.findByNom(command.produit())
        .orElseThrow(() -> new ProduitError("Produit " + command.produit() + " non trouvé"));

    if (produit.getCategorieVehicules().isEmpty()) {
      throw new ProduitError("Produit " + command.produit() + " n'a pas de catégorie de véhicule");
    }

    if (!produit.contientCategorie(command.categorie())) {
      throw new ProduitError(
          "Produit " + command.produit() + " n'a pas la catégorie de véhicule "
              + command.categorie());
    }
    return produit;
  }
}
