package ci.komobe.actionelle.domain.repositories;

import java.util.List;

import ci.komobe.actionelle.domain.entities.Assure;

/**
 * @author Moro KONÉ 2025-05-28
 */
public interface AssureRepository {
  boolean existsByNumeroCarteIdentite(String numeroCarteIdentite);

  void save(Assure assure);

  List<Assure> findAll();
}
