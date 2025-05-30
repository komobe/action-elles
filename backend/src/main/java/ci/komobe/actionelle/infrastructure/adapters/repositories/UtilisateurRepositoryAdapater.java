package ci.komobe.actionelle.infrastructure.adapters.repositories;

import ci.komobe.actionelle.application.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.infrastructure.adapters.mappers.UtilisateurMapper;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.UtilisateurJpaEntity;
import ci.komobe.actionelle.infrastructure.hibernatejpa.repositories.UtilisateurJpaRepository;
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
    UtilisateurJpaEntity entity = utilisateurMapper.toEntity(utilisateur);
    utilisateurJpaRepository.save(entity);
  }

  @Override
  public Optional<Utilisateur> findByUsername(String username) {
    return utilisateurJpaRepository.findByUsername(username)
        .map(utilisateurMapper::toDomain);
  }
}
