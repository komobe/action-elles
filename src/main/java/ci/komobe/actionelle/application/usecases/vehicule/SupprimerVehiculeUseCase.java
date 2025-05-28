package ci.komobe.actionelle.application.usecases.vehicule;

import ci.komobe.actionelle.application.commands.vehicule.SupprimerVehiculeCommand;
import ci.komobe.actionelle.application.exceptions.VehiculeError;
import ci.komobe.actionelle.application.repositories.VehiculeRepository;

/**
 * @author Moro KONÉ 2025-05-28
 */
public record SupprimerVehiculeUseCase(VehiculeRepository vehiculeRepository) {

  public void execute(SupprimerVehiculeCommand<?> command) {
    vehiculeRepository.findBySpecification(command)
        .orElseThrow(() -> new VehiculeError(
            "Le véhicule est introuvable avec les critères: "
                + command.field() + " = " + command.value()));

    vehiculeRepository.delete(command);
  }
}
