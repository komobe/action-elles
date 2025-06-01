package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Assure;
import java.util.List;

/**
 * @author Moro KONÉ 2025-05-28
 */
public interface AssureRepository extends CrudRepository<Assure> {

  boolean existsByNumeroCarteIdentite(String numeroCarteIdentite);

  List<Assure> findAll();
}
