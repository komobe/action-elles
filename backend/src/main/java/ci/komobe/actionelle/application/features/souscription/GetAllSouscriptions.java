package ci.komobe.actionelle.application.features.souscription;

import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.entities.Souscription;
import java.util.Collection;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-31
 */
@AllArgsConstructor
public class GetAllSouscriptions {

  private final SouscriptionRepository souscriptionRepository;

  public Collection<Souscription> get() {
    return souscriptionRepository.findAll();
  }
}
