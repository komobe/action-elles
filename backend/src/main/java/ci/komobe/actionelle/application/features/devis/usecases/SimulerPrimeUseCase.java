package ci.komobe.actionelle.application.features.devis.usecases;

import ci.komobe.actionelle.application.commons.services.prime.PrimeCalculator;
import ci.komobe.actionelle.application.features.devis.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.application.features.devis.dto.SimulationPrimeResult;
import ci.komobe.actionelle.application.features.devis.presenters.SimulerPrimePresenter;
import ci.komobe.actionelle.application.features.vehicule.CategorieVehiculeException;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.exceptions.ProduitErreur;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import ci.komobe.actionelle.domain.utils.QuoteReferenceGenerator;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * UseCase pour simuler la prime d'un produit d'assurance
 *
 * @param produitRepository le authenticationStrategy des produits
 * @param primeCalculator   le calculateur de prime
 * @author Moro KONÉ 2025-05-29
 */
public record SimulerPrimeUseCase(
    ProduitRepository produitRepository,
    PrimeCalculator primeCalculator,
    SimulerPrimePresenter<?> presenter
) {

  public void execute(SimulerPrimeCommand command) {
    Produit produit = getProduit(command);

    BigDecimal primeTotale = primeCalculator.calculPrime(produit, command);

    var simulationPrimeResult = SimulationPrimeResult.builder()
        .quoteReference(QuoteReferenceGenerator.generate())
        .price(primeTotale)
        .endDate(LocalDate.now().plusWeeks(2))
        .metadata(command)
        .build();

    presenter.addData(simulationPrimeResult);
  }

  private Produit getProduit(SimulerPrimeCommand command) {
    Produit produit = produitRepository.chercherParNom(command.getProduit())
        .orElseThrow(() -> new ProduitErreur("Produit non trouvé"));

    if (produit.contientCategorie(command.getCategorie())) {
      return produit;
    }

    throw new CategorieVehiculeException("Produit catégorie non trouvée");
  }
}
