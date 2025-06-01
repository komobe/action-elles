package ci.komobe.actionelle.domain.repositories;

import java.util.List;
import java.util.Optional;

import ci.komobe.actionelle.domain.entities.Produit;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface ProduitRepository extends CrudRepository<Produit> {
  Optional<Produit> recupererParNom(String code);

  List<Produit> recupererTous();

  boolean existsByNom(String code);
}
