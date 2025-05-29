package ci.komobe.actionelle.application.usecases;

import ci.komobe.actionelle.application.commands.CreerSouscriptionCommand;
import ci.komobe.actionelle.application.commands.assure.AssureCommandBase;
import ci.komobe.actionelle.application.commands.vehicule.VehiculeCommandBase;
import ci.komobe.actionelle.application.exceptions.AssureError;
import ci.komobe.actionelle.application.exceptions.VehiculeError;
import ci.komobe.actionelle.application.repositories.AssureRepository;
import ci.komobe.actionelle.application.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.application.repositories.SouscriptionRepository;
import ci.komobe.actionelle.application.repositories.VehiculeRepository;
import ci.komobe.actionelle.application.usecases.assure.AssureFactory;
import ci.komobe.actionelle.application.usecases.vehicule.VehiculeFactory;
import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.valueobjects.CategorieVehicule;

/**
 * Usecase pour créer une souscription
 * 
 * @param vehiculeRepository          le repository des véhicules
 * @param assureRepository            le repository des assurés
 * @param souscriptionRepository      le repository des souscriptions
 * @param categorieVehiculeRepository le repository des catégories de véhicules
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

    souscriptionRepository.save(souscription);
  }

  private CategorieVehicule getCategorieVehicule(String categorieCode) {
    return categorieVehiculeRepository
        .findByCode(categorieCode)
        .orElseThrow(
            () -> new VehiculeError("La catégorie de véhicule " + categorieCode + " n'existe pas"));
  }
}
