package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.utils.FakeGenerator;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-30
 */
public class InMemoryUtilisateurRepository implements UtilisateurRepository {

  private final Map<String, Utilisateur> utilisateurs = FakeGenerator.generateUtilisateurs();

  @Override
  public boolean existParUsername(String username) {
    return utilisateurs.containsKey(username);
  }

  @Override
  public void enregistrer(Utilisateur utilisateur) {
    utilisateurs.put(utilisateur.getUsername(), utilisateur);
  }

  @Override
  public void supprimer(Utilisateur entity) {
    utilisateurs.remove(entity.getUsername());
  }

  @Override
  public Optional<Utilisateur> chercherParUsername(String username) {
    return Optional.ofNullable(utilisateurs.get(username));
  }

  @Override
  public List<Utilisateur> lister() {
    return utilisateurs.values().stream().toList();
  }

  @Override
  public Page<Utilisateur> lister(Pageable pageable) {
    return Page.<Utilisateur>builder()
        .number(pageable.getNumber())
        .size(pageable.getSize())
        .totalElements(lister().size())
        .totalPages(1)
        .build();
  }

  @Override
  public Optional<Utilisateur> chercherParId(String utilisateurId) {
    return utilisateurs.values().stream()
        .filter(utilisateur -> utilisateur.getId().equals(utilisateurId))
        .findFirst();
  }

  @Override
  public boolean existeParId(String id) {
    return utilisateurs.values().stream()
        .anyMatch(utilisateur -> utilisateur.getId().equals(id));
  }
}
