package ci.komobe.actionelle.domain.repositories;

import java.util.List;
import java.util.Optional;

import ci.komobe.actionelle.domain.entities.Garantie;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface GarantieRepository {
  Optional<Garantie> findByCode(String code);

  List<Garantie> findAll();

  void save(Garantie garantie);

  boolean existsByCode(String code);
}
