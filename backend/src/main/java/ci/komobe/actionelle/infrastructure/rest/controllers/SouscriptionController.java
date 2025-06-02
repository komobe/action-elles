package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.application.commons.services.attestation.AttestationFormat;
import ci.komobe.actionelle.application.commons.services.attestation.AttestationGeneratorService;
import ci.komobe.actionelle.application.features.souscription.commands.CreerSouscriptionCommand;
import ci.komobe.actionelle.application.features.souscription.usecases.CreerSouscriptionUseCase;
import ci.komobe.actionelle.application.features.souscription.usecases.GenererAttestationUseCase;
import ci.komobe.actionelle.application.features.souscription.usecases.ListerSouscriptionUseCase;
import ci.komobe.actionelle.application.features.souscription.usecases.RecupererSouscriptionParId;
import ci.komobe.actionelle.application.features.souscription.usecases.RejeterSouscriptionUseCase;
import ci.komobe.actionelle.application.features.souscription.usecases.ValiderSouscriptionUseCase;
import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur de gestion des souscriptions
 *
 * @author Moro KONÉ 2025-05-29
 */
@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Souscriptions", description = "API de gestion des souscriptions d'assurance")
public class SouscriptionController {

  private final AttestationGeneratorService attestationService;

  private final VehiculeRepository vehiculeRepository;
  private final AssureRepository assureRepository;
  private final SouscriptionRepository souscriptionRepository;
  private final CategorieVehiculeRepository categorieVehiculeRepository;
  private final ProduitRepository produitRepository;

  @PostMapping
  public void creerSouscription(@RequestBody @Valid CreerSouscriptionCommand command) {
    var useCase = new CreerSouscriptionUseCase(
        vehiculeRepository,
        assureRepository,
        souscriptionRepository,
        categorieVehiculeRepository,
        produitRepository);
    useCase.execute(command);
  }

  @GetMapping
  public Collection<Souscription> listerSouscriptions() {
    return new ListerSouscriptionUseCase(souscriptionRepository).executer();
  }

  @PatchMapping("/{souscriptionId}/validate")
  public void validerSouscription(@PathVariable String souscriptionId) {
    new ValiderSouscriptionUseCase(souscriptionRepository).execute(souscriptionId);
  }

  @PatchMapping("/{souscriptionId}/reject")
  public void rejeterSouscription(@PathVariable String souscriptionId) {
    new RejeterSouscriptionUseCase(souscriptionRepository).execute(souscriptionId);
  }

  @GetMapping("/{souscriptionId}")
  public Souscription recupererSouscriptionParId(@PathVariable String souscriptionId) {
    return new RecupererSouscriptionParId(souscriptionRepository).recuperer(souscriptionId);
  }

  @GetMapping("/status/{souscriptionId}")
  public Souscription recupererStatutSouscriptionParId(@PathVariable String souscriptionId) {
    return new RecupererSouscriptionParId(souscriptionRepository).recuperer(souscriptionId);
  }

  @GetMapping("/{id}/attestation")
  public ResponseEntity<byte[]> genererAttestation(
      @PathVariable("id") String souscriptionId,
      @RequestParam(defaultValue = "PDF") AttestationFormat format) {
    var useCase = new GenererAttestationUseCase(souscriptionRepository, attestationService);

    byte[] content = useCase.execute(souscriptionId, format);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(getMediaType(format));
    headers.setContentDispositionFormData("attachment",
        String.format("attestation.%s", getFileExtension(format)));

    return ResponseEntity.ok()
        .headers(headers)
        .body(content);
  }

  private MediaType getMediaType(AttestationFormat format) {
    return switch (format) {
      case PDF -> MediaType.APPLICATION_PDF;
      case HTML -> MediaType.TEXT_HTML;
      case JSON -> MediaType.APPLICATION_JSON;
      case EXCEL -> MediaType.parseMediaType(
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    };
  }

  private String getFileExtension(AttestationFormat format) {
    return format.name().toLowerCase();
  }
}
