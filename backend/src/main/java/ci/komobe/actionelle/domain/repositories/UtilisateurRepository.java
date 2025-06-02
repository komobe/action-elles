package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-30
 */
public interface UtilisateurRepository extends BaseRepository<Utilisateur, String> {

  boolean existParUsername(String username);

  Optional<Utilisateur> chercherParUsername(String username);
}
