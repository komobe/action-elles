package ci.komobe.actionelle.application.commons.services.attestation;

import ci.komobe.actionelle.application.features.souscription.dto.AttestationDto;

/**
 * Interface pour la génération d'attestation
 * Cette interface définit la stratégie de génération d'attestation
 * 
 * @author Moro KONÉ 2025-06-01
 */
public interface AttestationGenerator {
  /**
   * Génère une attestation au format spécifié
   * 
   * @param data Les données de l'attestation
   * @return Le contenu de l'attestation au format bytes
   */
  byte[] generate(AttestationDto data);

  /**
   * @return Le type de format de l'attestation
   */
  AttestationFormat getFormat();
}