package ci.komobe.actionelle.infrastructure.persistences.jpa.repositories;

import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.VehiculeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface VehiculeJpaRepository
    extends JpaRepository<VehiculeEntity, String>, JpaSpecificationExecutor<VehiculeEntity> {

  boolean existsByImmatriculation(String numeroImmatriculation);

  Optional<VehiculeEntity> findByImmatriculation(String numeroImmatriculation);
}
