package ci.komobe.actionelle.infrastructure.persistences.jpa.repositories;

import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.AssureEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface AssureJpaRepository extends JpaRepository<AssureEntity, String> {
  boolean existsByNumeroCarteIdentite(String numeroCarteIdentite);

  Optional<AssureEntity> findByNumeroCarteIdentite(String numero);

  boolean existsByEmail(String email);
}
