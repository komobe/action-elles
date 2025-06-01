package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.domain.repositories.AssureRepository;
import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.infrastructure.mappers.AssureMapper;
import ci.komobe.actionelle.infrastructure.persistences.postgres.repositories.AssureJpaRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Repository
@AllArgsConstructor
public class AssureRepositoryAdapter implements AssureRepository {

  private final AssureMapper assureMapper;
  private final AssureJpaRepository assureJpaRepository;

  @Override
  public boolean existsByNumeroCarteIdentite(String numeroCarteIdentite) {
    return assureJpaRepository.existsByNumeroCarteIdentite(numeroCarteIdentite);
  }

  @Override
  public void enregistrer(Assure assure) {
    var assureEntity = assureMapper.toEntity(assure);
    assureJpaRepository.save(assureEntity);
  }

  @Override
  public List<Assure> findAll() {
    return assureJpaRepository.findAll().stream()
        .map(assureMapper::toDomain)
        .toList();
  }
}
