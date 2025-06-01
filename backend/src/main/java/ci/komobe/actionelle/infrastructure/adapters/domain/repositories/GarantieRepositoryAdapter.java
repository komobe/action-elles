package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.domain.repositories.GarantieRepository;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.infrastructure.mappers.GarantieMapper;
import ci.komobe.actionelle.infrastructure.persistences.postgres.repositories.GarantieJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Repository
@AllArgsConstructor
public class GarantieRepositoryAdapter implements GarantieRepository {

  private final GarantieMapper garantieMapper;
  private final GarantieJpaRepository garantieJpaRepository;

  @Override
  public Optional<Garantie> recupererParCode(String code) {
    return garantieJpaRepository.findByCode(code).map(garantieMapper::toDomain);
  }

  @Override
  public List<Garantie> findAll() {
    return garantieJpaRepository.findAll().stream()
        .map(garantieMapper::toDomain)
        .toList();
  }

  @Override
  public void enregistrer(Garantie garantie) {
    var garantieEntity = garantieMapper.toEntity(garantie);
    garantieJpaRepository.save(garantieEntity);
  }

  @Override
  public boolean codeExiste(String code) {
    return garantieJpaRepository.existsByCode(code);
  }
}
