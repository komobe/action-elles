package ci.komobe.actionelle.application.features.vehicule.usecases;

import ci.komobe.actionelle.application.features.vehicule.commands.SupprimerVehiculeCommand;
import ci.komobe.actionelle.application.features.vehicule.VehiculeError;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;

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
