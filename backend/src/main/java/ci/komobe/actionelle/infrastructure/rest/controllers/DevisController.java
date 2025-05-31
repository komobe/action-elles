package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.application.features.devis.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.application.features.devis.commands.EnregistrerDevisCommand;
import ci.komobe.actionelle.application.features.devis.presenters.DefaultSimulerPrimePresenter;
import ci.komobe.actionelle.domain.repositories.DevisRepository;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import ci.komobe.actionelle.application.commons.services.prime.PrimeCalculator;
import ci.komobe.actionelle.application.commons.services.prime.PrimeMontantFixeStrategy;
import ci.komobe.actionelle.application.commons.services.prime.PrimePourcentageStrategy;
import ci.komobe.actionelle.application.features.devis.usecases.EnregistrerDevisUseCase;
import ci.komobe.actionelle.application.features.devis.usecases.SimulerPrimeUseCase;
import ci.komobe.actionelle.application.features.devis.dto.SimulationPrimeResult;
import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Moro KONÉ 2025-05-29
 */
@RestController
@RequestMapping("/api/v1/devis")
public class DevisController {

  private final ProduitRepository produitRepository;
  private final PrimeCalculator primeCalculator;
  private final DevisRepository devisRepository;

  public DevisController(ProduitRepository produitRepository, DevisRepository devisRepository) {
    this.produitRepository = produitRepository;
    this.primeCalculator = new PrimeCalculator();
    this.primeCalculator.addStrategy(TypeMontantPrime.MONTANT, new PrimeMontantFixeStrategy());
    this.primeCalculator.addStrategy(TypeMontantPrime.POURCENTAGE, new PrimePourcentageStrategy());
    this.devisRepository = devisRepository;
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
        devisRepository);
    useCase.execute(command);
  }

  /*
   * @GetMapping("/{id}")
   * public ResponseEntity<Simulation> getSimulation(@PathVariable String id) {
   * var useCase = new ObtenirSimulationUseCase(simulationRepository);
   * useCase.execute(id);
   * }
   */
}
