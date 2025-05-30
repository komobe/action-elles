package ci.komobe.actionelle.application.repositories;

import ci.komobe.actionelle.application.utils.FakeGenerator;
import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class InMemoryCategorieVehiculeRepository implements CategorieVehiculeRepository {

  private final Map<String, CategorieVehicule> categorieVehicules = new HashMap<>();

  @Getter
  private int saveCallCount = 0;

  public InMemoryCategorieVehiculeRepository() {
    FakeGenerator.generateCategorieVehicules().forEach(this::addCategorieVehicule);
  }

  public void addCategorieVehicule(CategorieVehicule categorieVehicule) {
    categorieVehicules.put(categorieVehicule.getCode(), categorieVehicule);
  }

  public CategorieVehicule findRandomCategorieVehicule() {
    return categorieVehicules.values().stream().findAny().orElse(null);
  }

  public String findAnyCode() {
    return categorieVehicules.keySet().stream().findAny().orElse(null);
  }

  @Override
  public Optional<CategorieVehicule> findByCode(String code) {
    return categorieVehicules.containsKey(code)
        ? Optional.of(categorieVehicules.get(code))
        : Optional.empty();
  }

  @Override
  public void save(CategorieVehicule categorieVehicule) {
    categorieVehicules.put(categorieVehicule.getCode(), categorieVehicule);
    saveCallCount++;
  }

  @Override
  public boolean existsByCode(String code) {
    return categorieVehicules.containsKey(code);
  }

  public List<CategorieVehicule> findAll() {
    return new ArrayList<>(categorieVehicules.values());
  }
}
