package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.utils.FakeGenerator;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class InMemoryProduitRepository implements ProduitRepository {

  private final Map<String, Produit> produits = new HashMap<>();

  public InMemoryProduitRepository() {
    FakeGenerator.generateProduits().forEach(this::addProduit);
  }

  public void addProduit(Produit produit) {
    this.produits.put(produit.getNom(), produit);
  }

  @Override
  public void enregistrer(Produit produit) {
    addProduit(produit);
  }

  @Override
  public void supprimer(Produit entity) {
    produits.remove(entity.getNom());
  }

  @Override
  public Optional<Produit> chercherParId(String id) {
    return produits.values().stream()
        .filter(produit -> produit.getId().equals(id))
        .findFirst();
  }

  @Override
  public Optional<Produit> chercherParNom(String nom) {
    return produits.containsKey(nom) ? Optional.of(produits.get(nom)) : Optional.empty();
  }

  @Override
  public boolean existParNom(String nom) {
    return produits.containsKey(nom);
  }

  @Override
  public boolean existeParId(String id) {
    return produits.values().stream().anyMatch(p -> p.getId().equals(id));
  }

  @Override
  public boolean existeParCode(String code) {
    return produits.values().stream().anyMatch(p -> p.getCode().equals(code));
  }

  @Override
  public List<Produit> lister() {
    return new ArrayList<>(produits.values());
  }

  @Override
  public Page<Produit> lister(Pageable pageable) {
    var content = produits.values().stream()
        .skip(pageable.getOffset())
        .limit(pageable.getSize())
        .toList();
    int totalElements = produits.size();
    int totalPages = (int) Math.ceil((double) totalElements / pageable.getSize());
    return Page.<Produit>builder()
        .data(content)
        .number(pageable.getNumber())
        .size(pageable.getSize())
        .totalElements(totalElements)
        .totalPages(totalPages)
        .build();
  }
}
