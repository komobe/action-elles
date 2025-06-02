package ci.komobe.actionelle.application.features.vehicule.commands;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Commande pour créer un véhicule
 *
 * @author Moro KONÉ 2025-06-02
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreerVehiculeCommand {

  @NotBlank(message = "L'immatriculation est obligatoire")
  private String immatriculation;

  @NotBlank(message = "La couleur est obligatoire")
  private String couleur;

  @Min(value = 1, message = "Le nombre de sièges doit être supérieur à 0")
  @Max(value = 100, message = "Le nombre de sièges doit être inférieur à 100")
  private int nombreDeSieges;

  @Min(value = 1, message = "Le nombre de portes doit être supérieur à 0")
  @Max(value = 10, message = "Le nombre de portes doit être inférieur à 10")
  private int nombreDePortes;
  @NotBlank(message = "La catégorie est requise")
  private String categorieCode;

  @NotNull(message = "La date de mise en circulation est obligatoire")
  @Past(message = "La date de mise en circulation doit être dans le passé")
  private LocalDate dateMiseEnCirculation;

  @NotNull(message = "La puissance fiscale est obligatoire")
  @Min(value = 1, message = "La puissance fiscale doit être supérieure à 0")
  private Integer puissanceFiscale;

  @NotNull(message = "La valeur est obligatoire")
  private BigDecimal valeurNeuf;
}
