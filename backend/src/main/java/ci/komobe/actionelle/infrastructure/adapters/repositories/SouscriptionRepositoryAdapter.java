package ci.komobe.actionelle.infrastructure.adapters.repositories;

import ci.komobe.actionelle.application.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.infrastructure.adapters.mappers.SouscriptionMapper;
import ci.komobe.actionelle.infrastructure.hibernatejpa.repositories.AssureJpaRepository;
import ci.komobe.actionelle.infrastructure.hibernatejpa.repositories.SouscriptionJpaRepository;
import ci.komobe.actionelle.infrastructure.hibernatejpa.repositories.VehiculeJpaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Repository
@AllArgsConstructor
public class SouscriptionRepositoryAdapter implements SouscriptionRepository {

  private final SouscriptionMapper souscriptionMapper;
  private final SouscriptionJpaRepository souscriptionJpaRepository;
  private final AssureJpaRepository assureJpaRepository;
  private final VehiculeJpaRepository vehiculeJpaRepository;

  @Transactional
  @Override
  public void save(Souscription souscription) {
    var souscriptionEntity = souscriptionMapper.toEntity(souscription);
    assureJpaRepository.save(souscriptionEntity.getAssure());
    vehiculeJpaRepository.save(souscriptionEntity.getVehicule());
    souscriptionJpaRepository.save(souscriptionEntity);
  }

  @Override
  public List<Souscription> findAll() {
    return souscriptionJpaRepository.findAll().stream()
        .map(souscriptionMapper::toDomain)
        .toList();
  }

  @Override
  public Optional<Souscription> findById(String souscriptionId) {
    return souscriptionJpaRepository.findById(souscriptionId)
        .map(souscriptionMapper::toDomain);
  }
}
