package ci.komobe.actionelle.infrastructure.adapters.repositories;

import ci.komobe.actionelle.application.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.infrastructure.adapters.mappers.SouscriptionMapper;
import ci.komobe.actionelle.infrastructure.hibernatejpa.repositories.SouscriptionJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Repository
public class SouscriptionRepositoryAdapter implements SouscriptionRepository {

  private final SouscriptionMapper souscriptionMapper;
  private final SouscriptionJpaRepository souscriptionJpaRepository;

  public SouscriptionRepositoryAdapter(SouscriptionMapper souscriptionMapper,
      SouscriptionJpaRepository souscriptionJpaRepository) {
    this.souscriptionMapper = souscriptionMapper;
    this.souscriptionJpaRepository = souscriptionJpaRepository;
  }

  @Override
  public void save(Souscription souscription) {
    var souscriptionEntity = souscriptionMapper.toEntity(souscription);
    souscriptionJpaRepository.save(souscriptionEntity);
  }

  @Override
  public List<Souscription> findAll() {
    return souscriptionJpaRepository.findAll().stream()
        .map(souscriptionMapper::toDomain)
        .toList();
  }
}
