package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
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
public class InMemoryCategorieVehiculeRepository implements CategorieVehiculeRepository {

  private final Map<String, CategorieVehicule> categories = new HashMap<>();

  public InMemoryCategorieVehiculeRepository() {
    FakeGenerator.generateCategorieVehicules().forEach(this::addCategorie);
  }

  public void addCategorie(CategorieVehicule categorie) {
    this.categories.put(categorie.getCode(), categorie);
  }

  @Override
  public void enregistrer(CategorieVehicule categorie) {
    addCategorie(categorie);
  }

  @Override
  public void supprimer(CategorieVehicule entity) {
    categories.remove(entity.getCode());
  }

  @Override
  public Optional<CategorieVehicule> chercherParId(String id) {
    return categories.values().stream()
        .filter(categorie -> categorie.getId().equals(id))
        .findFirst();
  }

  @Override
  public Optional<CategorieVehicule> chercherParCode(String code) {
    return categories.containsKey(code) ? Optional.of(categories.get(code)) : Optional.empty();
  }

  @Override
  public boolean existeParCode(String code) {
    return categories.containsKey(code);
  }

  @Override
  public List<CategorieVehicule> lister() {
    return new ArrayList<>(categories.values());
  }

  @Override
  public boolean existeParId(String id) {
    return categories.values().stream()
        .anyMatch(categorie -> categorie.getId().equals(id));
  }

  @Override
  public Page<CategorieVehicule> lister(Pageable pageable) {
    return Page.<CategorieVehicule>builder()
        .number(pageable.getNumber())
        .size(pageable.getSize())
        .totalElements(lister().size())
        .totalPages(1)
        .build();
  }
}
