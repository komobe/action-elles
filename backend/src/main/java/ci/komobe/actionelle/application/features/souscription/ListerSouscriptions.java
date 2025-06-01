package ci.komobe.actionelle.application.features.souscription;

import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.entities.Souscription;
import java.util.Collection;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-31
 */
@AllArgsConstructor
public class ListerSouscriptions {

  private final SouscriptionRepository souscriptionRepository;

  public Collection<Souscription> executer() {
    return souscriptionRepository.findAll();
  }
}
