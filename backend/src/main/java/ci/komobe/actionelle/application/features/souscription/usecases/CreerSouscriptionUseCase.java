package ci.komobe.actionelle.application.features.souscription.usecases;

import ci.komobe.actionelle.application.features.assure.commands.CreerSouscriptionCommand;
import ci.komobe.actionelle.application.features.assure.commands.AssureCommandBase;
import ci.komobe.actionelle.application.features.vehicule.commands.VehiculeCommandBase;
import ci.komobe.actionelle.application.features.assure.AssureError;
import ci.komobe.actionelle.application.features.vehicule.VehiculeError;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
import ci.komobe.actionelle.application.features.assure.usecases.AssureFactory;
import ci.komobe.actionelle.application.features.vehicule.VehiculeFactory;
import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.entities.CategorieVehicule;

/**
 * Usecase pour créer une souscription
 * 
 * @param vehiculeRepository          le authenticationStrategy des véhicules
 * @param assureRepository            le authenticationStrategy des assurés
 * @param souscriptionRepository      le authenticationStrategy des souscriptions
 * @param categorieVehiculeRepository le authenticationStrategy des catégories de véhicules
 * 
 * @author Moro KONÉ 2025-05-28
 */
public record CreerSouscriptionUseCase(
    VehiculeRepository vehiculeRepository,
    AssureRepository assureRepository,
    SouscriptionRepository souscriptionRepository,
    CategorieVehiculeRepository categorieVehiculeRepository) {

  public void execute(CreerSouscriptionCommand command) {
    VehiculeCommandBase vehiculeData = command.getVehicule();
    AssureCommandBase assureData = command.getAssure();
    String immatriculation = vehiculeData.getNumeroImmatriculation();
    var vehiculeExists = vehiculeRepository.existsByImmatriculation(immatriculation);

    if (vehiculeExists) {
      throw new VehiculeError(
          "Le véhicule avec l'immatriculation " + immatriculation + " existe déjà");
    }

    String numeroCarteIdentite = assureData.getNumeroCarteIdentite();
    var assureExists = assureRepository.existsByNumeroCarteIdentite(numeroCarteIdentite);

    if (assureExists) {
      throw new AssureError(
          "L'assure avec le numéro de carte d'identité " + numeroCarteIdentite + " existe déjà");
    }

    CategorieVehicule categorieVehicule = getCategorieVehicule(vehiculeData.getCategorieCode());

    Vehicule vehicule = VehiculeFactory.factory(vehiculeData, categorieVehicule);
    Assure assure = AssureFactory.factory(assureData);

    var souscription = Souscription.creer(assure, vehicule);

    souscriptionRepository.enregistrer(souscription);
  }

  private CategorieVehicule getCategorieVehicule(String categorieCode) {
    return categorieVehiculeRepository
        .recupererParCode(categorieCode)
        .orElseThrow(
            () -> new VehiculeError("La catégorie de véhicule " + categorieCode + " n'existe pas"));
  }
}
