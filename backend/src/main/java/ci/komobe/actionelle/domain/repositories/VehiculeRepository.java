package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.application.commons.Specification;
import ci.komobe.actionelle.domain.entities.Vehicule;
import java.util.Optional;

/**
 * Interface pour le repository des véhicules
 *
 * @author Moro KONÉ 2025-06-01
 */
public interface VehiculeRepository extends BaseRepository<Vehicule, String> {

  boolean existParImmatriculation(String numero);

  Optional<Vehicule> chercherParImmatriculation(String numero);

  Optional<Vehicule> chercherParSpec(Specification<Vehicule> specification);
}
