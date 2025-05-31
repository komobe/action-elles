package ci.komobe.actionelle.infrastructure.persistences.postgres.repositories;

import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.AssureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface AssureJpaRepository extends JpaRepository<AssureEntity, String> {
  boolean existsByNumeroCarteIdentite(String numeroCarteIdentite);
}
