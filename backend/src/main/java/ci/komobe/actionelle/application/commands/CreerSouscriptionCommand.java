package ci.komobe.actionelle.application.commands;

import ci.komobe.actionelle.application.commands.assure.AssureCommandBase;
import ci.komobe.actionelle.application.commands.vehicule.VehiculeCommandBase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author Moro KONÉ 2025-05-28
 */
@Getter
@SuperBuilder
@AllArgsConstructor
public class CreerSouscriptionCommand {

  @Valid
  @NotNull(message = "Le véhicule ne peut pas être null")
  private VehiculeCommandBase vehicule;

  @Valid
  @NotNull(message = "L'assure ne peut pas être null")
  private AssureCommandBase assure;
}
