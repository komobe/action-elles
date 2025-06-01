package ci.komobe.actionelle.domain.repositories;

import java.util.List;
import java.util.Optional;

import ci.komobe.actionelle.domain.entities.Garantie;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface GarantieRepository extends CrudRepository<Garantie>{
  Optional<Garantie> recupererParCode(String code);

  List<Garantie> findAll();

  boolean codeExiste(String code);
}
