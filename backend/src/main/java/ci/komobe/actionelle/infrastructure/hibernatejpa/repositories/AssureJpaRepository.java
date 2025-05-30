package ci.komobe.actionelle.infrastructure.hibernatejpa.repositories;

import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.AssureEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface AssureJpaRepository extends JpaRepository<AssureEntityJpa, String> {
  boolean existsByNumeroCarteIdentite(String numeroCarteIdentite);
}
