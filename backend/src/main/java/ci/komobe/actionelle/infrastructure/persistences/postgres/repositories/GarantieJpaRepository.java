package ci.komobe.actionelle.infrastructure.persistences.postgres.repositories;

import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.GarantieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface GarantieJpaRepository extends JpaRepository<GarantieEntity, String> {
  boolean existsByCode(String code);

  Optional<GarantieEntity> findByCode(String code);
}
