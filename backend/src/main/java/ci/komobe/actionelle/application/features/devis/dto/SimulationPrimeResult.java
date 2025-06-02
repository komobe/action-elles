package ci.komobe.actionelle.application.features.devis.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;

/**
 * Résultat d'une simulation de prime d'assurance
 * 
 * @author Moro KONÉ 2025-05-30
 */
@Value
@Builder
public class SimulationPrimeResult {
  String quoteReference;
  BigDecimal price;
  LocalDate endDate;
  Object metadata;
}
