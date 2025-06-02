package ci.komobe.actionelle.infrastructure.persistences.jpa.repositories;

import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.DevisEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-30
 */
public interface DevisJpaRepository extends JpaRepository<DevisEntity, String> {
  boolean existsByReference(String reference);

  Optional<DevisEntity> findByReference(String reference);
}
