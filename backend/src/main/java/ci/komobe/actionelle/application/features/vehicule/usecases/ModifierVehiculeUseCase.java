package ci.komobe.actionelle.application.features.vehicule.usecases;

import ci.komobe.actionelle.application.features.vehicule.CategorieVehiculeException;
import ci.komobe.actionelle.application.features.vehicule.VehiculeErreur;
import ci.komobe.actionelle.application.features.vehicule.commands.ModifierVehiculeCommand;
import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class ModifierVehiculeUseCase {

  private final VehiculeRepository vehiculeRepository;
  private final CategorieVehiculeRepository categorieVehiculeRepository;

  public ModifierVehiculeUseCase(
      VehiculeRepository vehiculeRepository,
      CategorieVehiculeRepository categorieVehiculeRepository
  ) {
    this.vehiculeRepository = vehiculeRepository;
    this.categorieVehiculeRepository = categorieVehiculeRepository;
  }

  public void execute(ModifierVehiculeCommand command) {
    var vehiculeData = command.getVehiculeData();
    var categorieVehicule = getCategorieVehiculeByCode(vehiculeData.getCategorieCode());
    var vehicule = getVehiculeByImmatriculation(vehiculeData.getImmatriculation());

    vehicule.setCouleur(vehiculeData.getCouleur());
    vehicule.setNombreDeSieges(vehiculeData.getNombreDeSieges());
    vehicule.setNombreDePortes(vehiculeData.getNombreDePortes());
    vehicule.setCategorie(categorieVehicule);

    vehiculeRepository.enregistrer(vehicule);
  }

  private Vehicule getVehiculeByImmatriculation(String immatriculation) {
    return vehiculeRepository.chercherParImmatriculation(immatriculation)
        .orElseThrow(() -> new VehiculeErreur(
            "Le véhicule avec l'immatriculation " + immatriculation + " n'existe pas"));
  }

  private CategorieVehicule getCategorieVehiculeByCode(String categorieCode) {
    return categorieVehiculeRepository.chercherParCode(categorieCode)
        .orElseThrow(() -> new CategorieVehiculeException(
            "La catégorie de véhicule " + categorieCode + " n'existe pas"));
  }
}
