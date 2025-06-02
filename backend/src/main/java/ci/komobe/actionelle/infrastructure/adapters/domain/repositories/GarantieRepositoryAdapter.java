package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.repositories.GarantieRepository;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import ci.komobe.actionelle.infrastructure.mappers.GarantieMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.mappers.PageMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.repositories.GarantieJpaRepository;
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
  public Optional<Garantie> chercherParCode(String code) {
    return garantieJpaRepository.findByCode(code).map(garantieMapper::toDomain);
  }

  @Override
  public List<Garantie> lister() {
    return garantieJpaRepository.findAll().stream()
        .map(garantieMapper::toDomain)
        .toList();
  }

  @Override
  public Page<Garantie> lister(Pageable pageable) {
    var springPageRequest = PageMapper.toSpringPageRequest(pageable);
    var springPage = garantieJpaRepository.findAll(springPageRequest);
    return PageMapper.fromSpringPage(springPage, garantieMapper::toDomain);
  }

  @Override
  public void enregistrer(Garantie garantie) {
    var garantieEntity = garantieMapper.toEntity(garantie);
    garantieJpaRepository.save(garantieEntity);
  }

  @Override
  public void supprimer(Garantie entity) {
    garantieJpaRepository.delete(garantieMapper.toEntity(entity));
  }

  @Override
  public Optional<Garantie> chercherParId(String id) {
    return garantieJpaRepository.findById(id).map(garantieMapper::toDomain);
  }

  @Override
  public boolean existeParId(String id) {
    return garantieJpaRepository.existsById(id);
  }

  @Override
  public boolean existeParCode(String code) {
    return garantieJpaRepository.existsByCode(code);
  }
}
