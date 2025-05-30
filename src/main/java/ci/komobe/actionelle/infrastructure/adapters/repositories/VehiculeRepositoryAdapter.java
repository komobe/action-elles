package ci.komobe.actionelle.infrastructure.adapters.repositories;

import ci.komobe.actionelle.application.Specification;
import ci.komobe.actionelle.application.repositories.VehiculeRepository;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.infrastructure.adapters.mappers.VehiculeMapper;
import ci.komobe.actionelle.infrastructure.hibernatejpa.repositories.VehiculeJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Repository
@AllArgsConstructor
public class VehiculeRepositoryAdapter implements VehiculeRepository {

  private final VehiculeJpaRepository vehiculeJpaRepository;
  private final VehiculeMapper vehiculeMapper;

  @Override
  public void save(Vehicule vehicule) {
    var vehiculeEntity = vehiculeMapper.toEntity(vehicule);
    vehiculeJpaRepository.save(vehiculeEntity);
  }

  @Override
  public List<Vehicule> findAll() {
    return vehiculeJpaRepository.findAll().stream()
        .map(vehiculeMapper::toDomain).toList();
  }

  @Override
  public boolean existsByImmatriculation(String numero) {
    return vehiculeJpaRepository.existsByNumeroImmatriculation(numero);
  }

  @Override
  public Optional<Vehicule> findByImmatriculation(String numero) {
    return vehiculeJpaRepository.findByNumeroImmatriculation(numero)
        .map(vehiculeMapper::toDomain);
  }

  @Override
  public void delete(Specification<Vehicule> specification) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Optional<Vehicule> findBySpecification(Specification<Vehicule> specification) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
