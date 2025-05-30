package ci.komobe.actionelle.infrastructure.hibernatejpa.repositories;

import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.SouscriptionEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface SouscriptionJpaRepository extends JpaRepository<SouscriptionEntityJpa, String> {
  boolean existsByNumero(String numero);

  Optional<SouscriptionEntityJpa> findByNumero(String numero);
}
