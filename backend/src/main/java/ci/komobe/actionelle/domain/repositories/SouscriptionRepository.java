package ci.komobe.actionelle.domain.repositories;

import ci.komobe.actionelle.domain.entities.Souscription;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour le repository des souscriptions
 * 
 * @author Moro KONÉ 2025-06-01
 */
public interface SouscriptionRepository extends BaseRepository<Souscription, String>{

  Optional<Souscription> chercherParNumero(String numero);

  boolean existeParNumero(String numero);

  List<Souscription> lister();
}
