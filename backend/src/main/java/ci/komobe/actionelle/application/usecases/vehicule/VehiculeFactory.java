package ci.komobe.actionelle.application.usecases.vehicule;

import ci.komobe.actionelle.application.commands.vehicule.VehiculeCommandBase;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import ci.komobe.actionelle.domain.entities.CategorieVehicule;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class VehiculeFactory {

  private VehiculeFactory() {
  }

  public static Vehicule factory(VehiculeCommandBase command) {
    var vehicule = new Vehicule(IdGenerator.generateId());
    vehicule.setDateMiseEnCirculation(command.getDateMiseEnCirculation());
    vehicule.setNumeroImmatriculation(command.getNumeroImmatriculation());
    vehicule.setCouleur(command.getCouleur());
    vehicule.setNombreDeSieges(command.getNombreDeSieges());
    vehicule.setNombreDePortes(command.getNombreDePortes());
    return vehicule;
  }

  public static Vehicule factory(VehiculeCommandBase command, CategorieVehicule categorieVehicule) {
    var vehicule = factory(command);
    vehicule.setCategorie(categorieVehicule);
    return vehicule;
  }
}
