package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.infrastructure.mappers.UtilisateurMapper;
import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.UtilisateurEntity;
import ci.komobe.actionelle.infrastructure.persistences.postgres.repositories.UtilisateurJpaRepository;
import java.util.Collection;
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
  public boolean existsByUsername(String username) {
    return utilisateurJpaRepository.existsByUsername(username);
  }

  @Override
  public void save(Utilisateur utilisateur) {
    UtilisateurEntity entity = utilisateurMapper.toEntity(utilisateur);
    utilisateurJpaRepository.save(entity);
  }

  @Override
  public Optional<Utilisateur> findByUsername(String username) {
    return utilisateurJpaRepository.findByUsername(username)
        .map(utilisateurMapper::toDomain);
  }

  @Override
  public Collection<Utilisateur> findAll() {
    return utilisateurJpaRepository.findAll().stream()
        .map(utilisateurMapper::toDomain).toList();
  }

  @Override
  public Optional<Utilisateur> findById(String utilisateurId) {
    return utilisateurJpaRepository.findById(utilisateurId)
        .map(utilisateurMapper::toDomain);
  }
}
