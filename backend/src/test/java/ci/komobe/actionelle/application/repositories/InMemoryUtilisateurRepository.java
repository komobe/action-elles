package ci.komobe.actionelle.application.repositories;

import ci.komobe.actionelle.application.utils.FakeGenerator;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-30
 */
public class InMemoryUtilisateurRepository implements UtilisateurRepository {

  private final Map<String, Utilisateur> utilisateurs = FakeGenerator.generateUtilisateurs();

  @Override
  public boolean existsByUsername(String username) {
    return utilisateurs.containsKey(username);
  }

  @Override
  public void save(Utilisateur utilisateur) {
    utilisateurs.put(utilisateur.getUsername(), utilisateur);
  }

  @Override
  public Optional<Utilisateur> findByUsername(String username) {
    return Optional.ofNullable(utilisateurs.get(username));
  }

  @Override
  public Collection<Utilisateur> findAll() {
    return utilisateurs.values();
  }

  @Override
  public  Optional<Utilisateur>  findById(String utilisateurId) {
    return utilisateurs.values().stream()
        .filter(utilisateur -> utilisateur.getId().equals(utilisateurId))
        .findFirst();
  }
}
