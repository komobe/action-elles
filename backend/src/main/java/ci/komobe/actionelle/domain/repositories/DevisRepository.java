package ci.komobe.actionelle.domain.repositories;

import java.util.Optional;

import ci.komobe.actionelle.domain.entities.Devis;

/**
 * @author Moro KONÉ 2025-05-30
 */
public interface DevisRepository {
  void save(Devis devis);

  boolean existsByReference(String reference);

  Optional<Devis> findByReference(String reference);
}
