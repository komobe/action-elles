package ci.komobe.actionelle.infrastructure.persistences.postgres.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Data
@Embeddable
public class VehiculeEmbeddable {

  @Column(name = "vehicule_puissance_fiscale", nullable = false)
  private int puissanceFiscale;

  @Column(name = "vehicule_date_mise_en_circulation", nullable = false)
  private LocalDate dateMiseEnCirculation;

  @Column(name = "vehicule_valeur_neuf", nullable = false)
  private BigDecimal valeurNeuf;

  @Column(name = "vehicule_valeur_venale", nullable = false)
  private BigDecimal valeurVenale;
}
