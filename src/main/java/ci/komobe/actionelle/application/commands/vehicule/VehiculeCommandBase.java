package ci.komobe.actionelle.application.commands.vehicule;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author Moro KONÉ 2025-05-28
 */
@Getter
@SuperBuilder
public abstract class VehiculeCommandBase {

  @NotNull
  private final LocalDate dateMiseEnCirculation;

  @NotBlank
  private final String numeroImmatriculation;

  @NotBlank
  private final String couleur;

  @Min(1)
  private final int nombreDeSieges;

  @Min(1)
  private final int nombreDePortes;

  @NotBlank
  private final String categorieCode;
}
