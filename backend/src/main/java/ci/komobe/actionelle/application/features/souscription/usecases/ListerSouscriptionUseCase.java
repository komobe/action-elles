package ci.komobe.actionelle.application.features.souscription.usecases;

import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import java.util.Collection;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-31
 */
@AllArgsConstructor
public class ListerSouscriptionUseCase {

  private final SouscriptionRepository souscriptionRepository;

  public Collection<Souscription> executer() {
    return souscriptionRepository.lister();
  }
}
