package ci.komobe.actionelle.application.usecases.vehicule;

import ci.komobe.actionelle.application.exceptions.VehiculeError;
import ci.komobe.actionelle.application.commands.vehicule.CreerVehiculeCommand;
import ci.komobe.actionelle.application.repositories.VehiculeRepository;
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

    Vehicule factory = VehiculeFactory.factory(command);

    vehiculeRepository.save(factory);
  }
}
