package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Assure;
import java.util.Optional;

/**
 * Interface pour le repository des assurés
 *
 * @author Moro KONÉ 2025-05-28
 */
public interface AssureRepository extends BaseRepository<Assure, String> {

  boolean existeParEmail(String email);
  
  Optional<Assure> chercherParNumeroCarteIdentite(String numero);

  boolean existeParNumeroCarteIdentite(String numero);
}
