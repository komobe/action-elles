package ci.komobe.actionelle.infrastructure.hibernatejpa.repositories;

import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.ProduitEntityJpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface ProduitJpaRepository extends JpaRepository<ProduitEntityJpa, String> {
  boolean existsByNom(String nom);

  Optional<ProduitEntityJpa> findByNom(String nom);
}
