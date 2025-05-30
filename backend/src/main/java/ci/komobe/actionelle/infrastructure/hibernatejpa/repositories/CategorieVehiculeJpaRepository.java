package ci.komobe.actionelle.infrastructure.hibernatejpa.repositories;

import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.CategorieVehiculeEntityJpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface CategorieVehiculeJpaRepository extends
    JpaRepository<CategorieVehiculeEntityJpa, String> {

  boolean existsByCode(String code);

  Optional<CategorieVehiculeEntityJpa> findByCode(String code);
}
