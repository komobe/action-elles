package ci.komobe.actionelle.application.valueobjects;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Builder
public record SimulationPrimeResult(
    String quoteReference,
    BigDecimal price,
    LocalDate endDate,
    Object metadata) {
}
