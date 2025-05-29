package ci.komobe.actionelle.infrastructure.hibernatejpa.repositories;

import java.util.Optional;

import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.VehiculeEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface VehiculeJpaRepository
    extends JpaRepository<VehiculeEntityJpa, String>, JpaSpecificationExecutor<VehiculeEntityJpa> {

  boolean existsByNumeroImmatriculation(String numeroImmatriculation);

  Optional<VehiculeEntityJpa> findByNumeroImmatriculation(String numeroImmatriculation);
}
