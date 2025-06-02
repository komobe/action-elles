package ci.komobe.actionelle.application.commons.services.pdf;

import ci.komobe.actionelle.application.features.souscription.dto.AttestationDto;

/**
 * Interface pour la génération de PDF
 * 
 * @author Moro KONÉ 2025-06-01
 */
public interface PDFGenerator {
  byte[] generateAttestation(AttestationDto data);
}