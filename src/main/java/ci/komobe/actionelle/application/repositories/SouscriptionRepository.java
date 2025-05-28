package ci.komobe.actionelle.application.repositories;

import java.util.List;

import ci.komobe.actionelle.domain.entities.Souscription;

/**
 * @author Moro KONÉ 2025-05-28
 */
public interface SouscriptionRepository {
  void save(Souscription souscription);

  List<Souscription> findAll();
}
