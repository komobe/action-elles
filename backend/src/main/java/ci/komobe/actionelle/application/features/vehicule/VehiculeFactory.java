package ci.komobe.actionelle.application.features.vehicule;

import ci.komobe.actionelle.application.features.vehicule.commands.CreerVehiculeCommand;
import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import ci.komobe.actionelle.domain.valueobjects.Valeur;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class VehiculeFactory {

  private VehiculeFactory() {
  }

  public static Vehicule factory(CreerVehiculeCommand command) {
    validatePuissanceFiscale(command.getPuissanceFiscale());

    var vehicule = new Vehicule();
    vehicule.setId(IdGenerator.generateId());
    vehicule.setDateMiseEnCirculation(command.getDateMiseEnCirculation());
    vehicule.setImmatriculation(command.getImmatriculation());
    vehicule.setCouleur(command.getCouleur());
    vehicule.setNombreDeSieges(command.getNombreDeSieges());
    vehicule.setNombreDePortes(command.getNombreDePortes());
    vehicule.setValeurNeuf(Valeur.of(command.getValeurNeuf(), "XOF"));
    vehicule.setPuissanceFiscale(command.getPuissanceFiscale());
    return vehicule;
  }

  public static Vehicule factory(CreerVehiculeCommand command, CategorieVehicule categorieVehicule) {
    var vehicule = factory(command);
    vehicule.setCategorie(categorieVehicule);
    return vehicule;
  }

  private static void validatePuissanceFiscale(int puissanceFiscale) {
    if (puissanceFiscale <= 0 || puissanceFiscale > 100) {
      throw new VehiculeErreur("La puissance fiscale doit être comprise entre 1 et 100");
    }
  }
}
