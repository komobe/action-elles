package ci.komobe.actionelle.infrastructure.persistences.jpa.repositories;

import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.SouscriptionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface SouscriptionJpaRepository extends JpaRepository<SouscriptionEntity, String> {
  boolean existsByNumero(String numero);

  Optional<SouscriptionEntity> findByNumero(String numero);
}
