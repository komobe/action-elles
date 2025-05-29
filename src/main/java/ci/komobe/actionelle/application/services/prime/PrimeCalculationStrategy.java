package ci.komobe.actionelle.application.services.prime;

import ci.komobe.actionelle.application.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.domain.entities.Garantie;
import java.math.BigDecimal;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface PrimeCalculationStrategy {

  boolean isApplicable(Garantie garantie);

  BigDecimal calculer(Garantie garantie, SimulerPrimeCommand command);
}
