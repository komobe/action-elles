package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Souscription;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-28
 */
public interface SouscriptionRepository extends CrudRepository<Souscription>{

  Collection<Souscription> findAll();

  Optional<Souscription> recupererParId(String souscriptionId);
}
