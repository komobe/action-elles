package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import ci.komobe.actionelle.infrastructure.mappers.SouscriptionMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.mappers.PageMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.repositories.AssureJpaRepository;
import ci.komobe.actionelle.infrastructure.persistences.jpa.repositories.SouscriptionJpaRepository;
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
@AllArgsConstructor
public class SouscriptionRepositoryAdapter implements SouscriptionRepository {

  private final SouscriptionMapper souscriptionMapper;
  private final SouscriptionJpaRepository souscriptionJpaRepository;
  private final AssureJpaRepository assureJpaRepository;
  private final VehiculeJpaRepository vehiculeJpaRepository;

  @Transactional
  @Override
  public void enregistrer(Souscription souscription) {
    var souscriptionEntity = souscriptionMapper.toEntity(souscription);
    souscriptionJpaRepository.save(souscriptionEntity);
  }

  @Override
  public void supprimer(Souscription entity) {
    souscriptionJpaRepository.delete(souscriptionMapper.toEntity(entity));
  }

  @Override
  public List<Souscription> lister() {
    return souscriptionJpaRepository.findAll().stream()
        .map(souscriptionMapper::toDomain)
        .toList();
  }

  @Override
  public Optional<Souscription> chercherParId(String souscriptionId) {
    return souscriptionJpaRepository.findById(souscriptionId)
        .map(souscriptionMapper::toDomain);
  }

  @Override
  public boolean existeParId(String id) {
    return souscriptionJpaRepository.existsById(id);
  }

  @Override
  public Page<Souscription> lister(Pageable pageRequest) {
    var springPageRequest = PageMapper.toSpringPageRequest(pageRequest);
    var springPage = souscriptionJpaRepository.findAll(springPageRequest);
    return PageMapper.fromSpringPage(springPage, souscriptionMapper::toDomain);
  }

  @Override
  public Optional<Souscription> chercherParNumero(String numero) {
    return souscriptionJpaRepository.findByNumero(numero)
        .map(souscriptionMapper::toDomain);
  }

  @Override
  public boolean existeParNumero(String numero) {
    return souscriptionJpaRepository.existsByNumero(numero);
  }
}
