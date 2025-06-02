package ci.komobe.actionelle.infrastructure.persistences.jpa.repositories;

import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.UtilisateurEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-30
 */
public interface UtilisateurJpaRepository extends JpaRepository<UtilisateurEntity, String> {

  boolean existsByUsername(String username);

  Optional<UtilisateurEntity> findByUsername(String username);
}
