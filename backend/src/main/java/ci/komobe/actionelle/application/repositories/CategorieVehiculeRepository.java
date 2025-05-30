package ci.komobe.actionelle.application.repositories;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;

import java.util.List;
import java.util.Optional;

public interface CategorieVehiculeRepository {
  void save(CategorieVehicule categorieVehicule);

  List<CategorieVehicule> findAll();

  boolean existsByCode(String code);

  Optional<CategorieVehicule> findByCode(String code);
}
