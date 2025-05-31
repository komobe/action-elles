package ci.komobe.actionelle.application.commands.vehicule;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Moro KONÉ 2025-05-28
 */
@Getter
@SuperBuilder
@NoArgsConstructor // Add this
public class VehiculeCommandBase {

  @NotNull(message = "La date de mise en circulation est requise")
  private LocalDate dateMiseEnCirculation;

  @NotBlank(message = "Le numéro d'immatriculation est requis")
  private String numeroImmatriculation;

  @NotBlank(message = "La couleur est requise")
  private String couleur;

  @Min(value = 1, message = "Le nombre de sièges doit être supérieur à 0")
  @Max(value = 100, message = "Le nombre de sièges doit être inférieur à 100")
  private int nombreDeSieges;

  @Min(value = 1, message = "Le nombre de portes doit être supérieur à 0")
  @Max(value = 10, message = "Le nombre de portes doit être inférieur à 10")
  private int nombreDePortes;

  @NotBlank(message = "La catégorie est requise")
  private String categorieCode;
}
