package ci.komobe.actionelle.application.features.devis.usecases;

import ci.komobe.actionelle.application.commons.services.prime.PrimeCalculator;
import ci.komobe.actionelle.application.features.devis.commands.EnregistrerDevisCommand;
import ci.komobe.actionelle.application.features.vehicule.CategorieVehiculeException;
import ci.komobe.actionelle.domain.entities.Devis;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.exceptions.DevisErreur;
import ci.komobe.actionelle.domain.exceptions.ProduitErreur;
import ci.komobe.actionelle.domain.repositories.DevisRepository;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import ci.komobe.actionelle.domain.valueobjects.Valeur;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-30
 */
@AllArgsConstructor
public class EnregistrerDevisUseCase {

  private final ProduitRepository produitRepository;
  private final PrimeCalculator primeCalculator;
  private final DevisRepository devisRepository;
  private final VehiculeRepository vehiculeRepository;

  @Transactional
  public void execute(EnregistrerDevisCommand command) {
    if (devisRepository.existeParReference(command.getQuoteReference())) {
      throw new DevisErreur("Le devis existe déjà");
    }

    var produit = produitRepository.chercherParNom(command.getProduit())
        .orElseThrow(() -> new ProduitErreur("Le produit n'existe pas"));

    var categorie = produit.recupererCategorieParCode(command.getCategorie())
        .orElseThrow(() -> new CategorieVehiculeException("La catégorie n'existe pas"));

    String vehiculeImmatriculation = command.getVehiculeImmatriculation();
    var vehiculeOptional = vehiculeRepository.chercherParImmatriculation(vehiculeImmatriculation);

    Vehicule vehicule;
    if (vehiculeOptional.isEmpty()) {
      vehicule = Vehicule.builder()
          .id(IdGenerator.generateId())
          .categorie(categorie)
          .immatriculation(vehiculeImmatriculation)
          .puissanceFiscale(command.getPuissanceFiscale())
          .dateMiseEnCirculation(command.getDateDeMiseEnCirculation())
          .valeurNeuf(Valeur.of(command.getValeurNeuf(), "XOF"))
          .build();
      vehiculeRepository.enregistrer(vehicule);
    } else {
      vehicule = vehiculeOptional.get();
    }

    // Recalcule de la prime pour validation
    BigDecimal primeRecalculer = primeCalculator.calculPrime(produit, command);

    if (primeRecalculer.compareTo(command.getPrice()) != 0) {
      throw new DevisErreur("La prime transmise est incorrecte ou a été modifiée");
    }

    var devis = Devis.builder()
        .id(IdGenerator.generateId())
        .reference(command.getQuoteReference())
        .produitId(produit.getId())
        .vehiculeId(vehicule.getId())
        .dateSimulation(command.getDateExpiration())
        .montantPrime(primeRecalculer)
        .vehiculeValeurVenale(Valeur.of(command.getValeurVenale(), "XOF"))
        .build();

    devisRepository.enregistrer(devis);
  }
}
