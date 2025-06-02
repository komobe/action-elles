package ci.komobe.actionelle.application.features.souscription.commands;

import ci.komobe.actionelle.application.features.assure.commands.CreerAssureCommand;
import ci.komobe.actionelle.application.features.vehicule.commands.CreerVehiculeCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Moro KONÉ 2025-05-28
 */
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CreerSouscriptionCommand {

  @Valid
  @NotNull(message = "Le véhicule ne peut pas être null")
  private CreerVehiculeCommand vehicule;

  @Valid
  @NotNull(message = "L'assuré ne peut pas être null")
  private CreerAssureCommand assure;

  @NotNull(message = "La valeur vénale du véhicule ne peut pas être null")
  @Min(value = 1, message = "La valeur vénale du véhicule ne peut pas être inférieure à 1 FCFA")
  private BigDecimal vehiculeValeurVenale;

  @NotNull(message = "Le produit ne peut pas être null")
  private String produit;
}