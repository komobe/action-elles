package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import ci.komobe.actionelle.infrastructure.mappers.AssureMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.mappers.PageMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.repositories.AssureJpaRepository;
import java.util.List;
import java.util.Optional;
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
  public void enregistrer(Assure assure) {
    var assureEntity = assureMapper.toEntity(assure);
    assureJpaRepository.save(assureEntity);
  }

  @Override
  public void supprimer(Assure entity) {
    assureJpaRepository.delete(assureMapper.toEntity(entity));
  }

  @Override
  public Optional<Assure> chercherParId(String id) {
    return assureJpaRepository.findById(id).map(assureMapper::toDomain);
  }

  @Override
  public boolean existeParId(String id) {
    return false;
  }

  @Override
  public List<Assure> lister() {
    return assureJpaRepository.findAll().stream()
        .map(assureMapper::toDomain)
        .toList();
  }

  @Override
  public Page<Assure> lister(Pageable pageable) {
    var springPageRequest = PageMapper.toSpringPageRequest(pageable);
    var springPage = assureJpaRepository.findAll(springPageRequest);
    return PageMapper.fromSpringPage(springPage, assureMapper::toDomain);
  }

  @Override
  public boolean existeParNumeroCarteIdentite(String numero) {
    return assureJpaRepository.existsByNumeroCarteIdentite(numero);
  }

  @Override
  public boolean existeParEmail(String email) {
    return assureJpaRepository.existsByEmail(email);
  }

  @Override
  public Optional<Assure> chercherParNumeroCarteIdentite(String numero) {
    return assureJpaRepository.findByNumeroCarteIdentite(numero)
        .map(assureMapper::toDomain);
  }
}
