package ci.komobe.actionelle.application.commands.vehicule;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author Moro KONÉ 2025-05-28
 */
@Getter
@SuperBuilder
public class VehiculeCommandBase {

  @NotNull(message = "La date de mise en circulation est requise")
  private final LocalDate dateMiseEnCirculation;

  @NotBlank(message = "Le numéro d'immatriculation est requis")
  private final String numeroImmatriculation;

  @NotBlank(message = "La couleur est requise")
  private final String couleur;

  @Min(value = 1, message = "Le nombre de sièges doit être supérieur à 0")
  @Max(value = 100, message = "Le nombre de sièges doit être inférieur à 10")
  private final int nombreDeSieges;

  @Min(value = 1, message = "Le nombre de portes doit être supérieur à 0")
  @Max(value = 10, message = "Le nombre de portes doit être inférieur à 10")
  private final int nombreDePortes;

  @NotBlank(message = "La catégorie est requise")
  private final String categorieCode;
}
