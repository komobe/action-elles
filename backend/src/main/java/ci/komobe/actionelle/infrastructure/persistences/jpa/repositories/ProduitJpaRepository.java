package ci.komobe.actionelle.infrastructure.persistences.jpa.repositories;

import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.ProduitEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface ProduitJpaRepository extends JpaRepository<ProduitEntity, String> {
  boolean existsByNom(String nom);

  Optional<ProduitEntity> findByNom(String nom);

  boolean existsByCode(String code);
}
