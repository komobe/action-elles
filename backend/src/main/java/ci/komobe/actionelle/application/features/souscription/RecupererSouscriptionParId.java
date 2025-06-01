package ci.komobe.actionelle.application.features.souscription;

import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.entities.Souscription;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-31
 */
@AllArgsConstructor
public class RecupererSouscriptionParId {

  private final SouscriptionRepository souscriptionRepository;

  public  Souscription recuperer(String souscriptionId) {
    return souscriptionRepository.recupererParId(souscriptionId)
        .orElseThrow(() -> new IllegalArgumentException("Souscription inconnue"));
  }
}
