package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Devis;
import java.util.Optional;

/**
 * Interface pour le repository des devis
 *
 * @author Moro KONÉ 2025-06-01
 */
public interface DevisRepository extends BaseRepository<Devis, String> {

  boolean existeParReference(String reference);

  Optional<Devis> chercherParReference(String reference);
}
