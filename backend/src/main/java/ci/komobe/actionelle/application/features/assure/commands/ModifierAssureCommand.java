package ci.komobe.actionelle.application.features.assure.commands;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Commande pour modifier un assuré
 *
 * @author Moro KONÉ 2025-06-01
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifierAssureCommand {

  @NotBlank(message = "L'ID ne peut pas être vide")
  private String id;

  @Valid
  @NotNull(message = "Les données du véhicule ne peuvent pas être null")
  private CreerAssureCommand assureData;
}