package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.application.commons.Specification;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import ci.komobe.actionelle.infrastructure.mappers.VehiculeMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.mappers.PageMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.repositories.VehiculeJpaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Repository
@Transactional
@AllArgsConstructor
public class VehiculeRepositoryAdapter implements VehiculeRepository {

  private final VehiculeJpaRepository vehiculeJpaRepository;
  private final VehiculeMapper vehiculeMapper;

  @Override
  public void enregistrer(Vehicule vehicule) {
    var vehiculeEntity = vehiculeMapper.toEntity(vehicule);
    vehiculeJpaRepository.save(vehiculeEntity);
  }

  @Override
  public void supprimer(Vehicule entity) {
    vehiculeJpaRepository.delete(vehiculeMapper.toEntity(entity));
  }

  @Override
  public Optional<Vehicule> chercherParId(String id) {
    return vehiculeJpaRepository.findById(id).map(vehiculeMapper::toDomain);
  }

  @Override
  public List<Vehicule> lister() {
    return vehiculeJpaRepository.findAll().stream()
        .map(vehiculeMapper::toDomain).toList();
  }

  @Override
  public boolean existParImmatriculation(String numero) {
    return vehiculeJpaRepository.existsByImmatriculation(numero);
  }

  @Override
  public Optional<Vehicule> chercherParImmatriculation(String numero) {
    return vehiculeJpaRepository.findByImmatriculation(numero)
        .map(vehiculeMapper::toDomain);
  }

  @Override
  public Optional<Vehicule> chercherParSpec(Specification<Vehicule> specification) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean existeParId(String id) {
    return vehiculeJpaRepository.existsById(id);
  }

  @Override
  public Page<Vehicule> lister(Pageable pageRequest) {
    var springPageRequest = PageMapper.toSpringPageRequest(pageRequest);
    var springPage = vehiculeJpaRepository.findAll(springPageRequest);
    return PageMapper.fromSpringPage(springPage, vehiculeMapper::toDomain);
  }
}
