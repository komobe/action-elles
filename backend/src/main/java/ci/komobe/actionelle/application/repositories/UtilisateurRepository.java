package ci.komobe.actionelle.application.repositories;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-30
 */
public interface UtilisateurRepository {

  boolean existsByUsername(String username);

  void save(Utilisateur utilisateur);

  Optional<Utilisateur> findByUsername(String username);

  Collection<Utilisateur> findAll();

  Optional<Utilisateur>  findById(String utilisateurId);
}
