package ci.komobe.actionelle.infrastructure.persistences.jpa.repositories;

import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.GarantieEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface GarantieJpaRepository extends JpaRepository<GarantieEntity, String> {
  boolean existsByCode(String code);

  Optional<GarantieEntity> findByCode(String code);
}
