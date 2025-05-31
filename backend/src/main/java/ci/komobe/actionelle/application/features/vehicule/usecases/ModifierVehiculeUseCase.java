package ci.komobe.actionelle.application.features.vehicule.usecases;

import ci.komobe.actionelle.application.features.vehicule.commands.ModifierVehiculeCommand;
import ci.komobe.actionelle.application.features.vehicule.CategorieVehiculeError;
import ci.komobe.actionelle.application.features.vehicule.VehiculeError;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.entities.CategorieVehicule;

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
    var categorieVehicule = getCategorieVehiculeByCode(command.getCategorieCode());
    var vehicule = getVehiculeByImmatriculation(command.getNumeroImmatriculation());

    vehicule.setCouleur(command.getCouleur());
    vehicule.setNombreDeSieges(command.getNombreDeSieges());
    vehicule.setNombreDePortes(command.getNombreDePortes());
    vehicule.setCategorie(categorieVehicule);

    vehiculeRepository.save(vehicule);
  }

  private Vehicule getVehiculeByImmatriculation(String immatriculation) {
    return vehiculeRepository.findByImmatriculation(immatriculation)
        .orElseThrow(() -> new VehiculeError(
            "Le véhicule avec l'immatriculation " + immatriculation + " n'existe pas"));
  }

  private CategorieVehicule getCategorieVehiculeByCode(String categorieCode) {
    return categorieVehiculeRepository.findByCode(categorieCode)
        .orElseThrow(() -> new CategorieVehiculeError(
            "La catégorie de véhicule " + categorieCode + " n'existe pas"));
  }
}
