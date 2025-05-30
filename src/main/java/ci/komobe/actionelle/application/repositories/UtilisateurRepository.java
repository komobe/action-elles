package ci.komobe.actionelle.application.repositories;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-30
 */
public interface UtilisateurRepository {

  boolean existsByPseudo(String pseudo);

  void save(Utilisateur utilisateur);

  Optional<Utilisateur> findByPseudo(String pseudo);
}
