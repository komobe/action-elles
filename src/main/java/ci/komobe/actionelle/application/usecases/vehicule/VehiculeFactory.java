package ci.komobe.actionelle.application.usecases.vehicule;

import ci.komobe.actionelle.application.commands.vehicule.CreerVehiculeCommand;
import ci.komobe.actionelle.domain.entities.Vehicule;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class VehiculeFactory {

  private VehiculeFactory() {}

  public static Vehicule factory(CreerVehiculeCommand command) {
    var vehicule = new Vehicule();
    vehicule.setDateMiseEnCirculation(command.getDateMiseEnCirculation());
    vehicule.setNumeroImmatriculation(command.getNumeroImmatriculation());
    vehicule.setCouleur(command.getCouleur());
    vehicule.setNombreDeSieges(command.getNombreDeSieges());
    vehicule.setNombreDePortes(command.getNombreDePortes());
    return vehicule;
  }
}
