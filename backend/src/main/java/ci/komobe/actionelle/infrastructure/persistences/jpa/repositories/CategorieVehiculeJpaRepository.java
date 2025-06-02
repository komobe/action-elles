package ci.komobe.actionelle.infrastructure.persistences.jpa.repositories;

import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.CategorieVehiculeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface CategorieVehiculeJpaRepository extends
    JpaRepository<CategorieVehiculeEntity, String> {

  boolean existsByCode(String code);

  Optional<CategorieVehiculeEntity> findByCode(String code);
}
