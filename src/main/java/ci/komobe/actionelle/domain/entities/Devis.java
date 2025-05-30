package ci.komobe.actionelle.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Getter
@Setter
@Builder
public class Devis {

  private String id;
  private String reference;
  private String produitId;
  private String categorieId;
  private Integer puissanceFiscale;
  private BigDecimal vehiculeValeurNeuf;
  private BigDecimal vehiculeValeurVenale;
  private LocalDate vehiculeDateMiseEnCirculation;
  private LocalDate dateSimulation;
  private BigDecimal prime;
  private LocalDate dateExpiration;
}
