package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.application.features.assure.commands.CreerSouscriptionCommand;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
import ci.komobe.actionelle.application.features.souscription.usecases.CreerSouscriptionUseCase;
import ci.komobe.actionelle.application.features.souscription.GetAllSouscriptions;
import ci.komobe.actionelle.application.features.souscription.GetSouscriptionById;
import ci.komobe.actionelle.domain.entities.Souscription;
import jakarta.validation.Valid;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping
  public Collection<Souscription> listSouscriptions() {
    return new GetAllSouscriptions(souscriptionRepository).get();
  }

  @GetMapping("/{souscriptionId}")
  public Souscription getSouscriptionById(@PathVariable String souscriptionId) {
    return new GetSouscriptionById(souscriptionRepository).get(souscriptionId);
  }
}
