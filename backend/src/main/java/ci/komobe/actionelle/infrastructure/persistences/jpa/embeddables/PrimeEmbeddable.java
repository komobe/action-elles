package ci.komobe.actionelle.infrastructure.persistences.jpa.embeddables;

import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import ci.komobe.actionelle.infrastructure.persistences.jpa.converters.TypeMontantPrimeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Data
@Embeddable
public class PrimeEmbeddable {

  @Convert(converter = TypeMontantPrimeConverter.class)
  private TypeMontantPrime type;

  private BigDecimal valeur;
}
