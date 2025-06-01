package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import java.util.List;
import java.util.Optional;

public interface CategorieVehiculeRepository extends CrudRepository<CategorieVehicule> {

  List<CategorieVehicule> findAll();

  boolean codeExiste(String code);

  Optional<CategorieVehicule> recupererParCode(String code);
}
