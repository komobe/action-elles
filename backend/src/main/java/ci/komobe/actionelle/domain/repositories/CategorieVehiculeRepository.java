package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import java.util.Optional;

public interface CategorieVehiculeRepository extends BaseRepository<CategorieVehicule, String> {

  boolean existeParCode(String code);

  Optional<CategorieVehicule> chercherParCode(String code);
}
