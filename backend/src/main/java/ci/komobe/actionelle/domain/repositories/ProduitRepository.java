package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Produit;
import java.util.Optional;

/**
 * Interface pour le repository des produits
 *
 * @author Moro KONÉ 2025-06-01
 */
public interface ProduitRepository extends BaseRepository<Produit, String> {

  Optional<Produit> chercherParNom(String nom);

  boolean existParNom(String nom);

  boolean existeParCode(String code);
}
