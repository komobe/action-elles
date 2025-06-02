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
import java.math.BigDecimal;
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
    Vehicule vehicule = recupererVehicule(command.getVehicule());
    Assure assure = recupererAssure(command.getAssure());
    BigDecimal vehiculeValeurVenale = command.getVehiculeValeurVenale();

    if (vehiculeValeurVenale.compareTo(vehicule.getValeurNeuf().getMontant()) > 0) {
      throw new IllegalArgumentException("La valeur vénale du véhicule ne peut pas être supérieure à sa valeur à neuf");
    }

    Produit produit = produitRepository.chercherParNom(command.getProduit())
        .orElseThrow(
            () -> new ProduitErreur("Le produit " + command.getProduit() + " n'existe pas"));

    Souscription souscription = assure.souscrire(vehicule, produit, vehiculeValeurVenale);

    assureRepository.enregistrer(assure);
    vehiculeRepository.enregistrer(vehicule);
    souscriptionRepository.enregistrer(souscription);
  }

  private Assure recupererAssure(CreerAssureCommand assureData) {
    String numeroCarteIdentite = assureData.getNumeroCarteIdentite();
    var assureExistant = assureRepository.chercherParNumeroCarteIdentite(numeroCarteIdentite);
    Assure assureCreerDepuisCommande = AssureFactory.factory(assureData);
    Assure assureAEnregistrer = assureExistant.orElse(assureCreerDepuisCommande);
    assureAEnregistrer.mettreAjourDepuis(assureCreerDepuisCommande);
    return assureAEnregistrer;
  }

  private Vehicule recupererVehicule(CreerVehiculeCommand vehiculeData) {
    CategorieVehicule categorieVehicule = getCategorieVehicule(vehiculeData.getCategorieCode());
    String immatriculation = vehiculeData.getImmatriculation();
    var vehiculeExistant = vehiculeRepository.chercherParImmatriculation(immatriculation);
    Vehicule vehiculeCreerDepuisCommande = VehiculeFactory.factory(vehiculeData, categorieVehicule);
    Vehicule vehiculeAEnregistrer = vehiculeExistant.orElse(vehiculeCreerDepuisCommande);
    vehiculeAEnregistrer.mettreAjourDepuis(vehiculeCreerDepuisCommande);
    return vehiculeAEnregistrer;
  }

  private CategorieVehicule getCategorieVehicule(String categorieCode) {
    return categorieVehiculeRepository
        .chercherParCode(categorieCode)
        .orElseThrow(
            () -> new VehiculeErreur(
                "La catégorie de véhicule " + categorieCode + " n'existe pas"));
  }
}
