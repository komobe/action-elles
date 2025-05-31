package ci.komobe.actionelle.application.commons.services.prime;

import ci.komobe.actionelle.application.features.devis.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import java.math.BigDecimal;

/**
 * @author Moro KONÉ 2025-05-29
 */
public class PrimeMontantFixeStrategy implements PrimeCalculationStrategy {

  @Override
  public boolean isApplicable(Garantie garantie) {
    return TypeMontantPrime.MONTANT.equals(garantie.getPrime().type());
  }

  @Override
  public BigDecimal calculer(Garantie garantie, SimulerPrimeCommand command) {
    return garantie.getPrime().valeur();
  }
}
