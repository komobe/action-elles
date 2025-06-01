package ci.komobe.actionelle.domain.repositories;

import java.util.Optional;

import ci.komobe.actionelle.domain.entities.Devis;

/**
 * @author Moro KONÉ 2025-05-30
 */
public interface DevisRepository extends CrudRepository<Devis> {

  boolean referenceExiste(String reference);

  Optional<Devis> recupererParReference(String reference);
}
