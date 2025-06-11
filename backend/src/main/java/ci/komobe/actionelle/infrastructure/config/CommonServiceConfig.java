package ci.komobe.actionelle.infrastructure.config;

import ci.komobe.actionelle.application.commons.services.prime.PrimeCalculator;
import ci.komobe.actionelle.application.commons.services.prime.PrimeMontantFixeStrategy;
import ci.komobe.actionelle.application.commons.services.prime.PrimePourcentageStrategy;
import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Moro KONÉ 2025-06-11
 */
@Configuration
public class CommonServiceConfig {

  @Bean
  public PrimeCalculator primeCalculator() {
    return PrimeCalculator.builder()
        .addStrategy(TypeMontantPrime.POURCENTAGE, new PrimePourcentageStrategy())
        .addStrategy(TypeMontantPrime.MONTANT, new PrimeMontantFixeStrategy())
        .build();
  }
}
