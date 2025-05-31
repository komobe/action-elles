package ci.komobe.actionelle.domain.repositories;

import java.util.List;
import java.util.Optional;

import ci.komobe.actionelle.domain.entities.Produit;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface ProduitRepository {
  Optional<Produit> findByNom(String code);

  List<Produit> findAll();

  void save(Produit produit);

  boolean existsByNom(String code);
}
