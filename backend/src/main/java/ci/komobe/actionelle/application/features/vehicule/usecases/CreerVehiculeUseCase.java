package ci.komobe.actionelle.application.features.vehicule.usecases;

import ci.komobe.actionelle.application.features.vehicule.VehiculeFactory;
import ci.komobe.actionelle.application.features.vehicule.commands.CreerVehiculeCommand;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.exceptions.VehiculeErreur;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-28
 */
@RequiredArgsConstructor
public class CreerVehiculeUseCase {

  private final VehiculeRepository vehiculeRepository;

  public void execute(CreerVehiculeCommand command) throws VehiculeErreur {

    String immatriculation = command.getImmatriculation();
    boolean immatriculationExist = vehiculeRepository.existParImmatriculation(immatriculation);

    if (immatriculationExist) {
      throw new VehiculeErreur("Un véhicule immatriculé " + immatriculation + " déjà enregistré");
    }

    Vehicule vehicule = VehiculeFactory.factory(command);

    vehiculeRepository.enregistrer(vehicule);
  }
}
