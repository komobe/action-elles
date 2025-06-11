package ci.komobe.actionelle.application.features.souscription.usecases;

import ci.komobe.actionelle.application.features.assure.commands.CreerAssureCommand;
import ci.komobe.actionelle.application.features.assure.usecases.AssureFactory;
import ci.komobe.actionelle.application.features.souscription.commands.CreerSouscriptionCommand;
import ci.komobe.actionelle.application.features.vehicule.VehiculeErreur;
import ci.komobe.actionelle.application.features.vehicule.VehiculeFactory;
import ci.komobe.actionelle.application.features.vehicule.commands.CreerVehiculeCommand;
import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.exceptions.ProduitErreur;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/**
 * Usecase pour créer une souscription
 *
 * @author Moro KONÉ 2025-05-28
 */
@AllArgsConstructor
public class CreerSouscriptionUseCase {

  private final VehiculeRepository vehiculeRepository;
  private final AssureRepository assureRepository;
  private final SouscriptionRepository souscriptionRepository;
  private final CategorieVehiculeRepository categorieVehiculeRepository;
  private final ProduitRepository produitRepository;

  @Transactional
  public void execute(CreerSouscriptionCommand command) {
    Vehicule vehicule = obtenirOuCreerVehicule(command.getVehicule());
    Assure assure = obtenirOuCreerAssure(command.getAssure());
    Produit produit = obtenirProduit(command.getProduit());

    Souscription souscription = assure.souscrire(
        vehicule, produit,
        command.getVehiculeValeurVenale()
    );

    assureRepository.enregistrer(assure);
    vehiculeRepository.enregistrer(vehicule);
    souscriptionRepository.enregistrer(souscription);
  }

  private Produit obtenirProduit(String nomProduit) {
    return produitRepository.chercherParNom(nomProduit)
        .orElseThrow(
            () -> new ProduitErreur("Le produit " + nomProduit + " n'existe pas"));
  }

  private Assure obtenirOuCreerAssure(CreerAssureCommand assureData) {
    String numeroCarteIdentite = assureData.getNumeroCarteIdentite();
    var assureExistant = assureRepository.chercherParNumeroCarteIdentite(numeroCarteIdentite);
    Assure assureCreerDepuisCommande = AssureFactory.factory(assureData);
    Assure assureAEnregistrer = assureExistant.orElse(assureCreerDepuisCommande);
    assureAEnregistrer.mettreAjourDepuis(assureCreerDepuisCommande);
    return assureAEnregistrer;
  }

  private Vehicule obtenirOuCreerVehicule(CreerVehiculeCommand vehiculeData) {
    CategorieVehicule categorieVehicule = obtenirCategorieVehicule(vehiculeData.getCategorieCode());
    String immatriculation = vehiculeData.getImmatriculation();
    var vehiculeExistant = vehiculeRepository.chercherParImmatriculation(immatriculation);
    Vehicule vehiculeCreerDepuisCommande = VehiculeFactory.factory(vehiculeData, categorieVehicule);
    Vehicule vehiculeAEnregistrer = vehiculeExistant.orElse(vehiculeCreerDepuisCommande);
    vehiculeAEnregistrer.mettreAjourDepuis(vehiculeCreerDepuisCommande);
    return vehiculeAEnregistrer;
  }

  private CategorieVehicule obtenirCategorieVehicule(String categorieCode) {
    return categorieVehiculeRepository.chercherParCode(categorieCode)
        .orElseThrow(() -> new VehiculeErreur(
            "La catégorie de véhicule " + categorieCode + " n'existe pas"));
  }
}