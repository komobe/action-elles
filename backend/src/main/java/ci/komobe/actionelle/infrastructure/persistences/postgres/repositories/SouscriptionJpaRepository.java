package ci.komobe.actionelle.infrastructure.persistences.postgres.repositories;

import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.SouscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface SouscriptionJpaRepository extends JpaRepository<SouscriptionEntity, String> {
  boolean existsByNumero(String numero);

  Optional<SouscriptionEntity> findByNumero(String numero);
}
