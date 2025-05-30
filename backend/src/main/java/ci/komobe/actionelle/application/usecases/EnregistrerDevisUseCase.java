package ci.komobe.actionelle.application.usecases;

import ci.komobe.actionelle.application.commands.EnregistrerDevisCommand;
import ci.komobe.actionelle.application.exceptions.CategorieVehiculeError;
import ci.komobe.actionelle.application.exceptions.DevisError;
import ci.komobe.actionelle.application.exceptions.ProduitError;
import ci.komobe.actionelle.application.repositories.DevisRepository;
import ci.komobe.actionelle.application.repositories.ProduitRepository;
import ci.komobe.actionelle.application.services.prime.PrimeCalculator;
import ci.komobe.actionelle.domain.entities.Devis;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import java.math.BigDecimal;

/**
 * @author Moro KONÉ 2025-05-30
 */
public class EnregistrerDevisUseCase {

  private final ProduitRepository produitRepository;
  private final PrimeCalculator primeCalculator;
  private final DevisRepository devisRepository;

  public EnregistrerDevisUseCase(
      ProduitRepository produitRepository,
      PrimeCalculator primeCalculator,
      DevisRepository devisRepository
  ) {
    this.produitRepository = produitRepository;
    this.primeCalculator = primeCalculator;
    this.devisRepository = devisRepository;
  }

  public void execute(EnregistrerDevisCommand command) {
    if (devisRepository.existsByReference(command.getQuoteReference())) {
      throw new DevisError("Le devis existe déjà");
    }

    var produit = produitRepository.findByNom(command.getProduit())
        .orElseThrow(() -> new ProduitError("Le produit n'existe pas"));

    var categorie = produit.getCategorieVehicule(command.getCategorie())
        .orElseThrow(() -> new CategorieVehiculeError("La catégorie n'existe pas"));

    // Recalcule de la prime pour validation
    BigDecimal recalculatedPrime = primeCalculator.calculPrime(produit, command);

    if (recalculatedPrime.compareTo(command.getPrice()) != 0) {
      throw new DevisError("La prime transmise est incorrecte ou a été modifiée");
    }

    var devis = Devis.builder()
        .id(IdGenerator.generateId())
        .reference(command.getQuoteReference())
        .produitId(produit.getId())
        .categorieId(categorie.getId())
        .puissanceFiscale(command.getPuissanceFiscale())
        .vehiculeValeurNeuf(command.getValeurNeuf())
        .vehiculeValeurVenale(command.getValeurVenale())
        .vehiculeDateMiseEnCirculation(command.getDateDeMiseEnCirculation())
        .dateSimulation(command.getDateExpiration().minusWeeks(2))
        .prime(recalculatedPrime)
        .dateExpiration(command.getDateExpiration())
        .build();

    devisRepository.save(devis);
  }
}
