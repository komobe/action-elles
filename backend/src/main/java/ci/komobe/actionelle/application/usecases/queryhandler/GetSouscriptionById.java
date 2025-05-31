package ci.komobe.actionelle.application.usecases.queryhandler;

import ci.komobe.actionelle.application.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.entities.Souscription;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-31
 */
@AllArgsConstructor
public class GetSouscriptionById {

  private final SouscriptionRepository souscriptionRepository;

  public  Souscription get(String souscriptionId) {
    return souscriptionRepository.findById(souscriptionId)
        .orElseThrow(() -> new IllegalArgumentException("Souscription inconnue"));
  }
}
