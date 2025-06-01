package ci.komobe.actionelle.application.features.vehicule.usecases;

import ci.komobe.actionelle.application.features.vehicule.VehiculeError;
import ci.komobe.actionelle.application.features.vehicule.commands.CreerVehiculeCommand;
import ci.komobe.actionelle.application.features.vehicule.VehiculeFactory;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
import ci.komobe.actionelle.domain.entities.Vehicule;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class CreerVehiculeUseCase {

  private final VehiculeRepository vehiculeRepository;

  public CreerVehiculeUseCase(VehiculeRepository vehiculeRepository) {
    this.vehiculeRepository = vehiculeRepository;
  }

  public void execute(CreerVehiculeCommand command) throws VehiculeError {

    String immatriculation = command.getNumeroImmatriculation();
    boolean immatriculationExist = vehiculeRepository.existsByImmatriculation(immatriculation);

    if (immatriculationExist) {
      throw new VehiculeError("Un véhicule immatriculé " + immatriculation + " déjà enregistré");
    }

    Vehicule vehicule = VehiculeFactory.factory(command);

    vehiculeRepository.enregistrer(vehicule);
  }
}
