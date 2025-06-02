package ci.komobe.actionelle.application.features.devis.usecases;

import ci.komobe.actionelle.domain.entities.Devis;
import ci.komobe.actionelle.domain.exceptions.DevisErreur;
import ci.komobe.actionelle.domain.repositories.DevisRepository;
import lombok.RequiredArgsConstructor;

/**
 * Cas d'utilisation pour la récupération d'un devis
 * 
 * @author Moro KONÉ 2025-06-01
 */
@RequiredArgsConstructor
public class RecupererDevisUseCase {

  private final DevisRepository devisRepository;

  public Devis execute(String reference) {
    return devisRepository.chercherParReference(reference)
        .orElseThrow(() -> new DevisErreur("Devis non trouvé avec la référence: " + reference));
  }
}