package ci.komobe.actionelle.infrastructure.hibernatejpa.repositories;

import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.UserJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-30
 */
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, String> {

  Optional<UserJpaEntity> findByUsername(String username);

}
