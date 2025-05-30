package ci.komobe.actionelle.infrastructure.views.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ci.komobe.actionelle.application.commands.CreerSouscriptionCommand;
import ci.komobe.actionelle.application.repositories.AssureRepository;
import ci.komobe.actionelle.application.repositories.SouscriptionRepository;
import ci.komobe.actionelle.application.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.application.repositories.VehiculeRepository;
import ci.komobe.actionelle.application.usecases.CreerSouscriptionUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-29
 */
@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/v1/subscriptions")
public class SouscriptionController {

  private final VehiculeRepository vehiculeRepository;
  private final AssureRepository assureRepository;
  private final SouscriptionRepository souscriptionRepository;
  private final CategorieVehiculeRepository categorieVehiculeRepository;

  @PostMapping
  public void creerSouscription(@RequestBody @Valid CreerSouscriptionCommand command) {
    var useCase = new CreerSouscriptionUseCase(
        vehiculeRepository,
        assureRepository,
        souscriptionRepository,
        categorieVehiculeRepository);
    useCase.execute(command);
  }

  // TODO: Implementer les use cases pour les autres endpoints
  /*
   * 
   * @GetMapping("/{id}")
   * public void getSouscription(@PathVariable String id) {
   * var useCase = new ObtenirSouscriptionUseCase(souscriptionRepository);
   * useCase.execute(id);
   * }
   * 
   * @GetMapping("/status/{id}")
   * public void getSouscriptionStatus(@PathVariable String id) {
   * var useCase = new ObtenirSouscriptionStatusUseCase(souscriptionRepository);
   * useCase.execute(id);
   * }
   * 
   * @GetMapping("/{id}/attestation")
   * public void getSouscriptionAttestation(@PathVariable String id) {
   * var useCase = new
   * ObtenirSouscriptionAttestationUseCase(souscriptionRepository);
   * useCase.execute(id);
   * }
   * 
   */
}
