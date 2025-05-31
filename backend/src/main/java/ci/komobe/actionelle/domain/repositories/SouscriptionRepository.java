package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Souscription;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-28
 */
public interface SouscriptionRepository {

  void save(Souscription souscription);

  Collection<Souscription> findAll();

  Optional<Souscription> findById(String souscriptionId);
}
