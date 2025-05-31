package ci.komobe.actionelle.application.features.devis.commands;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

/**
 * @author Moro KONÉ 2025-05-29
 */

@Data
public class SimulerPrimeCommand {

  @NotBlank(message = "Le produit est requis")
  private String produit;

  @NotBlank(message = "La catégorie est requise")
  private String categorie;

  @Min(value = 1, message = "La puissance fiscale doit être supérieure à 0")
  @Max(value = 100, message = "La puissance fiscale doit être inférieure à 100")
  private int puissanceFiscale;

  @NotNull(message = "La date de mise en circulation est requise")
  private LocalDate dateDeMiseEnCirculation;

  @NotNull(message = "La valeur neuf est requise")
  private BigDecimal valeurNeuf;

  @NotNull(message = "La valeur venale est requise")
  private BigDecimal valeurVenale;
}
