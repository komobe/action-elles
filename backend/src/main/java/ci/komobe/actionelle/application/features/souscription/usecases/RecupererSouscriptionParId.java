package ci.komobe.actionelle.application.features.souscription.usecases;

import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-31
 */
@AllArgsConstructor
public class RecupererSouscriptionParId {

  private final SouscriptionRepository souscriptionRepository;

  public  Souscription recuperer(String souscriptionId) {
    return souscriptionRepository.chercherParId(souscriptionId)
        .orElseThrow(() -> new IllegalArgumentException("Souscription inconnue"));
  }
}
