package ci.komobe.actionelle.application.services.prime;

import ci.komobe.actionelle.application.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Moro KONÉ 2025-05-29
 */
public class PrimeCalculator {

  private final Map<TypeMontantPrime, PrimeCalculationStrategy> strategies = new EnumMap<>(TypeMontantPrime.class);

  public BigDecimal calculer(Garantie garantie, SimulerPrimeCommand command) {
    PrimeCalculationStrategy primeCalculationStrategy = strategies.get(garantie.getPrime().type());

    if (primeCalculationStrategy == null) {
      throw new IllegalStateException("No strategy found for type " + garantie.getPrime().type());
    }
    if (primeCalculationStrategy.isApplicable(garantie)) {
      return primeCalculationStrategy.calculer(garantie, command);
    }

    return BigDecimal.ZERO;
  }

  public void addStrategy(TypeMontantPrime typeMontantPrime, PrimeCalculationStrategy strategy) {
    strategies.put(typeMontantPrime, strategy);
  }
}
