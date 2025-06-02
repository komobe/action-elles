package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import ci.komobe.actionelle.infrastructure.mappers.UtilisateurMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.UtilisateurEntity;
import ci.komobe.actionelle.infrastructure.persistences.jpa.mappers.PageMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.repositories.UtilisateurJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Repository
@AllArgsConstructor
public class UtilisateurRepositoryAdapater implements UtilisateurRepository {

  private final UtilisateurMapper utilisateurMapper;
  private final UtilisateurJpaRepository utilisateurJpaRepository;

  @Override
  public boolean existParUsername(String username) {
    return utilisateurJpaRepository.existsByUsername(username);
  }

  @Override
  public void enregistrer(Utilisateur utilisateur) {
    UtilisateurEntity entity = utilisateurMapper.toEntity(utilisateur);
    utilisateurJpaRepository.save(entity);
  }

  @Override
  public Optional<Utilisateur> chercherParUsername(String username) {
    return utilisateurJpaRepository.findByUsername(username)
        .map(utilisateurMapper::toDomain);
  }

  @Override
  public List<Utilisateur> lister() {
    return utilisateurJpaRepository.findAll().stream()
        .map(utilisateurMapper::toDomain).toList();
  }

  @Override
  public Page<Utilisateur> lister(Pageable pageable) {
    var springPageRequest = PageMapper.toSpringPageRequest(pageable);
    var springPage = utilisateurJpaRepository.findAll(springPageRequest);
    return PageMapper.fromSpringPage(springPage, utilisateurMapper::toDomain);
  }

  @Override
  public Optional<Utilisateur> chercherParId(String utilisateurId) {
    return utilisateurJpaRepository.findById(utilisateurId)
        .map(utilisateurMapper::toDomain);
  }

  @Override
  public void supprimer(Utilisateur utilisateur) {
    var entity = utilisateurMapper.toEntity(utilisateur);
    utilisateurJpaRepository.delete(entity);
  }

  @Override
  public boolean existeParId(String id) {
    return utilisateurJpaRepository.existsById(id);
  }
}
