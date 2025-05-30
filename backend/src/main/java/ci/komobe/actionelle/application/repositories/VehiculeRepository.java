package ci.komobe.actionelle.application.repositories;

import ci.komobe.actionelle.application.Specification;
import ci.komobe.actionelle.domain.entities.Vehicule;
import java.util.List;
import java.util.Optional;

/**
 * @author Moro KONÉ 2025-05-28
 */
public interface VehiculeRepository {

  void save(Vehicule vehicule);

  List<Vehicule> findAll();

  boolean existsByImmatriculation(String numero);

  Optional<Vehicule> findByImmatriculation(String numero);

  void delete(Specification<Vehicule> specification);

  Optional<Vehicule> findBySpecification(Specification<Vehicule> specification);
}
