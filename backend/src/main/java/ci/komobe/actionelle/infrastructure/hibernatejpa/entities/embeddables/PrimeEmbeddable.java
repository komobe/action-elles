package ci.komobe.actionelle.infrastructure.hibernatejpa.entities.embeddables;

import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Data
@Embeddable
public class PrimeEmbeddable {

  @Enumerated(EnumType.STRING)
  private TypeMontantPrime type;

  private BigDecimal valeur;
}
