package ci.komobe.actionelle.infrastructure.hibernatejpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.DevisJpaEntity;

/**
 * @author Moro KONÉ 2025-05-30
 */
public interface DevisJpaRepository extends JpaRepository<DevisJpaEntity, String> {
  boolean existsByReference(String reference);

  Optional<DevisJpaEntity> findByReference(String reference);
}
