package ci.komobe.actionelle.application.services.prime;

import ci.komobe.actionelle.application.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.valueobjects.Prime;
import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Moro KONÉ 2025-05-29
 */
public class PrimePourcentageStrategy implements PrimeCalculationStrategy {

  @Override
  public boolean isApplicable(Garantie garantie) {
    return TypeMontantPrime.POURCENTAGE.equals(garantie.getPrime().type());
  }

  @Override
  public BigDecimal calculer(Garantie garantie, SimulerPrimeCommand command) {
    BigDecimal base = switch (garantie.getBaseDeCalcul()) {
      case VALEUR_A_NEUF -> command.valeurNeuf();
      case VALEUR_VENALE -> command.valeurVenale();
      case VALEUR_ASSUREE -> command.valeurVenale().multiply(BigDecimal.valueOf(0.5));
      default -> BigDecimal.ZERO;
    };

    BigDecimal garantieValeur = garantie.getPrime().valeur();
    BigDecimal taux = garantieValeur.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

    BigDecimal montant = base.multiply(taux);

    if (garantie.isPrimeMinimum()) {
      Prime primeMinimum = garantie.getPrimeMinimum();
      BigDecimal montantMinimum;
      if (TypeMontantPrime.POURCENTAGE.equals(primeMinimum.type())) {
        taux = primeMinimum.valeur().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        montantMinimum = base.multiply(taux);
      } else {
        montantMinimum = primeMinimum.valeur();
      }

      if (montantMinimum.compareTo(montant) > 0) {
        montant = montantMinimum;
      }
    }

    return montant;
  }
}
