package ci.komobe.actionelle.application.features.souscription.usecases;

import ci.komobe.actionelle.application.commons.services.attestation.AttestationFormat;
import ci.komobe.actionelle.application.commons.services.attestation.AttestationGeneratorService;
import ci.komobe.actionelle.application.features.souscription.dto.AttestationDto;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.exceptions.SouscriptionErreur;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import lombok.RequiredArgsConstructor;

/**
 * Use case pour générer une attestation
 *
 * @author Moro KONÉ 2025-06-01
 */
@RequiredArgsConstructor
public class GenererAttestationUseCase {

  private final SouscriptionRepository souscriptionRepository;
  private final AttestationGeneratorService attestationService;

  public byte[] execute(String souscriptionId, AttestationFormat format) {

    Souscription souscription = souscriptionRepository.chercherParNumero(souscriptionId)
        .orElseThrow(() -> new SouscriptionErreur("La souscription " + souscriptionId + " n'existe pas"));

    Vehicule vehicule = souscription.getVehicule();
    Produit produit = souscription.getProduit();

    // Préparer les données pour l'attestation
    var data = AttestationDto.builder()
        .souscriptionId(souscriptionId)
        .numero(souscriptionId)
        .vehicule(vehicule)
        .vehiculeValeurVenale(souscription.getVehiculeValeurVenale())
        .produit(produit)
        .build();

    // Générer l'attestation dans le format demandé
    return attestationService.generer(data, format);
  }
}