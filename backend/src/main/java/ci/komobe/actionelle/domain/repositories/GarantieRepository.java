package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Garantie;
import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface GarantieRepository extends BaseRepository<Garantie, String> {

  Optional<Garantie> chercherParCode(String code);

  boolean existeParCode(String code);
}
