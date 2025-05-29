package ci.komobe.actionelle.infrastructure.hibernatejpa.repositories;

import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.GarantieEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface GarantieJpaRepository extends JpaRepository<GarantieEntityJpa, String> {
  boolean existsByCode(String code);

  Optional<GarantieEntityJpa> findByCode(String code);
}
