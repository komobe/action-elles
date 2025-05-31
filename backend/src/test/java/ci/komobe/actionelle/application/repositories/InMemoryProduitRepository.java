package ci.komobe.actionelle.application.repositories;

import ci.komobe.actionelle.application.utils.FakeGenerator;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import java.util.List;
import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-29
 */
public class InMemoryProduitRepository implements ProduitRepository {

  private final List<Produit> produits = FakeGenerator.generateProduits();

  @Override
  public Optional<Produit> findByNom(String nom) {
    return produits.stream().filter(produit -> produit.getNom().equals(nom)).findFirst();
  }

  @Override
  public List<Produit> findAll() {
    return produits;
  }

  @Override
  public void save(Produit produit) {
    produits.add(produit);
  }

  @Override
  public boolean existsByNom(String nom) {
    return produits.stream().anyMatch(produit -> produit.getNom().equals(nom));
  }
}
