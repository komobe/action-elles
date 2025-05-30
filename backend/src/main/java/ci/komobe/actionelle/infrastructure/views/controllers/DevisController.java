package ci.komobe.actionelle.infrastructure.views.controllers;

import ci.komobe.actionelle.application.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.application.commands.EnregistrerDevisCommand;
import ci.komobe.actionelle.application.presenters.DefaultSimulerPrimePresenter;
import ci.komobe.actionelle.application.repositories.DevisRepository;
import ci.komobe.actionelle.application.repositories.ProduitRepository;
import ci.komobe.actionelle.application.services.prime.PrimeCalculator;
import ci.komobe.actionelle.application.services.prime.PrimeMontantFixeStrategy;
import ci.komobe.actionelle.application.services.prime.PrimePourcentageStrategy;
import ci.komobe.actionelle.application.usecases.EnregistrerDevisUseCase;
import ci.komobe.actionelle.application.usecases.SimulerPrimeUseCase;
import ci.komobe.actionelle.application.valueobjects.SimulationPrimeResult;
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
