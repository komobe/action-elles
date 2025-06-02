package ci.komobe.actionelle.application.features.vehicule.commands;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Commande pour modifier un véhicule
 *
 * @author Moro KONÉ 2025-06-01
 */
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ModifierVehiculeCommand {

  @NotBlank(message = "L'ID ne peut pas être vide")
  private String id;

  @Valid
  @NotNull(message = "Les données du véhicule ne peuvent pas être null")
  private CreerVehiculeCommand vehiculeData;
}
