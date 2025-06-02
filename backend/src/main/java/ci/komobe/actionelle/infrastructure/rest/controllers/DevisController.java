package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.application.commons.services.prime.PrimeCalculator;
import ci.komobe.actionelle.application.commons.services.prime.PrimeMontantFixeStrategy;
import ci.komobe.actionelle.application.commons.services.prime.PrimePourcentageStrategy;
import ci.komobe.actionelle.application.features.devis.commands.EnregistrerDevisCommand;
import ci.komobe.actionelle.application.features.devis.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.application.features.devis.dto.SimulationPrimeResult;
import ci.komobe.actionelle.application.features.devis.presenters.DefaultSimulerPrimePresenter;
import ci.komobe.actionelle.application.features.devis.usecases.EnregistrerDevisUseCase;
import ci.komobe.actionelle.application.features.devis.usecases.RecupererDevisUseCase;
import ci.komobe.actionelle.application.features.devis.usecases.SimulerPrimeUseCase;
import ci.komobe.actionelle.domain.entities.Devis;
import ci.komobe.actionelle.domain.repositories.DevisRepository;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des devis
 *
 * @author Moro KONÉ 2025-06-01
 */
@RestController
@RequestMapping("/api/v1/devis")
@Tag(name = "Devis", description = "API de gestion des devis et simulations")
public class DevisController {

  private final ProduitRepository produitRepository;
  private final DevisRepository devisRepository;
  private final VehiculeRepository vehiculeRepository;
  private final PrimeCalculator primeCalculator;

  public DevisController(
      ProduitRepository produitRepository,
      DevisRepository devisRepository,
      VehiculeRepository vehiculeRepository
  ) {
    this.produitRepository = produitRepository;
    this.devisRepository = devisRepository;
    this.vehiculeRepository = vehiculeRepository;
    this.primeCalculator = new PrimeCalculator();
    this.primeCalculator.addStrategy(TypeMontantPrime.POURCENTAGE, new PrimePourcentageStrategy());
    this.primeCalculator.addStrategy(TypeMontantPrime.MONTANT, new PrimeMontantFixeStrategy());
  }

  @PostMapping("/simuler")
  public SimulationPrimeResult simulePrime(@RequestBody @Valid SimulerPrimeCommand command) {
    var useCase = new SimulerPrimeUseCase(produitRepository, primeCalculator);
    var presenter = new DefaultSimulerPrimePresenter();
    useCase.execute(command, presenter);
    return presenter.present();
  }

  @PostMapping("/enregistrer")
  public void enregistrerDevis(@RequestBody @Valid EnregistrerDevisCommand command) {
    var useCase = new EnregistrerDevisUseCase(
        produitRepository,
        primeCalculator,
        devisRepository,
        vehiculeRepository);
    useCase.execute(command);
  }

  @GetMapping("/{reference}")
  public Devis getDevis(@PathVariable String reference) {
    var useCase = new RecupererDevisUseCase(devisRepository);
    return useCase.execute(reference);
  }
}
