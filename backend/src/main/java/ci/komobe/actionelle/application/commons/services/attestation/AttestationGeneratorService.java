package ci.komobe.actionelle.application.commons.services.attestation;

import ci.komobe.actionelle.application.features.souscription.dto.AttestationDto;
import java.util.List;

/**
 * Interface pour le service de génération d'attestation
 *
 * @author Moro KONÉ 2025-06-01
 */
public interface AttestationGeneratorService {

  /**
   * Génère une attestation au format PDF.
   *
   * @param attestationDto Les données de l'attestation
   * @return Le contenu du PDF généré
   */
  byte[] generer(AttestationDto attestationDto, AttestationFormat format);

  /**
   * @return La liste des formats supportés
   */
  List<AttestationFormat> getSupportedFormats();
}