package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.valueobjects.Valeur;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité Devis
 * 
 * @author Moro KONÉ 2025-06-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(buildMethodName = "_build")
public class Devis {
  private String id;
  private String reference;
  private String produitId;
  private String vehiculeId;
  private Valeur vehiculeValeurVenale;
  private BigDecimal montantPrime;
  private LocalDate dateSimulation;
  private LocalDate dateExpiration;


  public static class DevisBuilder {
    public Devis build() {
      if (Objects.nonNull(dateExpiration) && Objects.nonNull(dateSimulation)) {
        if (!dateSimulation.plusWeeks(2).equals(dateExpiration)) {
          throw new IllegalArgumentException(
              "La valeur de la date d'expiration doit être égale à la date de simulation + 2 semaines");
        }
      }

      if (Objects.nonNull(dateSimulation) && Objects.isNull(dateExpiration)) {
        this.dateExpiration = dateSimulation.plusWeeks(2);
      }

      if (Objects.nonNull(dateExpiration) && Objects.isNull(dateSimulation)) {
        this.dateSimulation = dateExpiration.minusWeeks(2);
      }

      if (Objects.isNull(dateExpiration)) {
        this.dateExpiration = LocalDate.now().plusWeeks(2);
        this.dateSimulation = LocalDate.now();
      }

      return _build();
    }
  }

  public void ajouterDateSimulation(LocalDate dateSimulation) {
    if (Objects.isNull(dateSimulation)) {
      this.dateExpiration = null;
      return;
    }

    this.dateSimulation = dateSimulation;
    this.dateExpiration = dateSimulation.plusWeeks(2);
  }

  public void ajouterDateExpriration(LocalDate dateExpriration) {
    if (Objects.isNull(dateExpriration)) {
      this.dateSimulation = null;
      return;
    }

    this.dateExpiration = dateExpriration;
    this.dateSimulation = dateExpriration.minusWeeks(2);
  }
}
