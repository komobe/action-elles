package ci.komobe.actionelle.infrastructure.persistences.postgres.repositories;

import java.util.Optional;

import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.VehiculeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface VehiculeJpaRepository
    extends JpaRepository<VehiculeEntity, String>, JpaSpecificationExecutor<VehiculeEntity> {

  boolean existsByNumeroImmatriculation(String numeroImmatriculation);

  Optional<VehiculeEntity> findByNumeroImmatriculation(String numeroImmatriculation);
}
