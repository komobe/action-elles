package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.application.commons.services.prime.PrimeCalculator;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RequestMapping("/api/v1/devis")
@Tag(name = "Devis", description = "API de gestion des devis et simulations")
public class DevisController {

  private final ProduitRepository produitRepository;
  private final DevisRepository devisRepository;
  private final VehiculeRepository vehiculeRepository;
  private final PrimeCalculator primeCalculator;

  @PostMapping("/simuler")
  public SimulationPrimeResult simulePrime(@RequestBody @Valid SimulerPrimeCommand command) {
    var presenter = new DefaultSimulerPrimePresenter();
    new SimulerPrimeUseCase(produitRepository, primeCalculator, presenter).execute(command);
    return presenter.present();
  }

  @PostMapping("/enregistrer")
  public void enregistrerDevis(@RequestBody @Valid EnregistrerDevisCommand command) {
    var useCase = new EnregistrerDevisUseCase(
        produitRepository,
        primeCalculator,
        devisRepository,
        vehiculeRepository
    );
    useCase.execute(command);
  }

  @GetMapping("/{reference}")
  public Devis getDevis(@PathVariable String reference) {
    var useCase = new RecupererDevisUseCase(devisRepository);
    return useCase.execute(reference);
  }
}
