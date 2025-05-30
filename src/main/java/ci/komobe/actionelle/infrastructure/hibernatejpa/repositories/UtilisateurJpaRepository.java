package ci.komobe.actionelle.infrastructure.hibernatejpa.repositories;

import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.UtilisateurJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-30
 */
public interface UtilisateurJpaRepository extends JpaRepository<UtilisateurJpaEntity, String> {

  boolean existsByUsername(String username);

  Optional<UtilisateurJpaEntity> findByUsername(String username);
}
