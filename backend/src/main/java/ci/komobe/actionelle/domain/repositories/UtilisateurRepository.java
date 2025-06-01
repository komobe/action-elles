package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.PageRequest;
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

  Page<Utilisateur> findAll(PageRequest pageRequest);

  Optional<Utilisateur> findById(String utilisateurId);
}
