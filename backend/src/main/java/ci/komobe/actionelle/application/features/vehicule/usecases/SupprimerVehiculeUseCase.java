package ci.komobe.actionelle.application.features.vehicule.usecases;

import ci.komobe.actionelle.application.features.vehicule.commands.SupprimerVehiculeCommand;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.exceptions.VehiculeErreur;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;

/**
 * @author Moro KONÉ 2025-05-28
 */
public record SupprimerVehiculeUseCase(VehiculeRepository vehiculeRepository) {

  public void execute(SupprimerVehiculeCommand<?> command) {
    Vehicule vehicule = vehiculeRepository.chercherParSpec(command)
        .orElseThrow(() -> new VehiculeErreur(
            "Le véhicule est introuvable avec les critères: "
                + command.field() + " = " + command.value()));

    vehiculeRepository.supprimer(vehicule);
  }
}
