package ci.komobe.actionelle.application.services.prime;

import ci.komobe.actionelle.application.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Moro KONÉ 2025-05-29
 */
public class PrimeCalculator {

  private static final double TAUX_CALCUL_VALEUR_ASSUREE = 0.5;

  private final Map<TypeMontantPrime, PrimeCalculationStrategy> strategies =
      new EnumMap<>(TypeMontantPrime.class);

  private BigDecimal calculer(Garantie garantie, SimulerPrimeCommand command) {
    PrimeCalculationStrategy strategy = strategies.get(garantie.getPrime().type());

    if (strategy == null) {
      throw new IllegalStateException(
          "Aucune stratégie trouvée pour le type " + garantie.getPrime().type());
    }

    if (strategy.isApplicable(garantie)) {
      return strategy.calculer(garantie, command);
    }

    return BigDecimal.ZERO;
  }

  public void addStrategy(TypeMontantPrime type, PrimeCalculationStrategy strategy) {
    strategies.put(type, strategy);
  }

  public BigDecimal calculPrime(Produit produit, SimulerPrimeCommand command) {
    double age = calculAge(command);
    BigDecimal totauxPrime = BigDecimal.ZERO;

    // Garantie RC
    Garantie garantieRc = produit.getGarantiesResponsabiliteCivile().stream()
        .filter(g -> !g.estSuperieurMaxAge(age))
        .filter(g -> g.getPuissanceFiscale().isInRange(command.getPuissanceFiscale()))
        .findFirst()
        .orElseThrow(() -> new RuntimeException(
            "Aucune garantie RC ne correspond à la puissance fiscale et à l'âge"));

    BigDecimal primeRc = calculer(garantieRc, command);
    totauxPrime = totauxPrime.add(primeRc);

    // Autres garanties
    for (Garantie garantie : produit.getAutresGaranties()) {
      if (garantie.estSuperieurMaxAge(age)) {
        continue;
      }

      BigDecimal montant = calculer(garantie, command);

      if (garantie.isPlafonne() && montant.compareTo(BigDecimal.ZERO) > 0) {
        BigDecimal valeurAssuree = command.getValeurVenale()
            .multiply(BigDecimal.valueOf(TAUX_CALCUL_VALEUR_ASSUREE));

        if (montant.compareTo(valeurAssuree) >= 0) {
          montant = valeurAssuree;
        }
      }

      totauxPrime = totauxPrime.add(montant);
    }

    return totauxPrime;
  }

  private double calculAge(SimulerPrimeCommand command) {
    Period period = Period.between(command.getDateDeMiseEnCirculation(), LocalDate.now());
    int years = period.getYears();
    int months = period.getMonths();
    int days = period.getDays();
    return years + (months / 12.0) + (days / 365.0);
  }
}
