package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import ci.komobe.actionelle.infrastructure.mappers.CategorieVehiculeMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.mappers.PageMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.repositories.CategorieVehiculeJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Repository
@AllArgsConstructor
public class CategorieVehiculeRepositoryAdapter implements CategorieVehiculeRepository {

  private final CategorieVehiculeMapper categorieVehiculeMapper;
  private final CategorieVehiculeJpaRepository categorieVehiculeJpaRepository;

  @Override
  public void enregistrer(CategorieVehicule categorieVehicule) {
    var categorieVehiculeEntity = categorieVehiculeMapper.toEntity(categorieVehicule);
    categorieVehiculeJpaRepository.save(categorieVehiculeEntity);
  }

  @Override
  public void supprimer(CategorieVehicule entity) {
    categorieVehiculeJpaRepository.delete(categorieVehiculeMapper.toEntity(entity));
  }

  @Override
  public Optional<CategorieVehicule> chercherParId(String id) {
    return categorieVehiculeJpaRepository.findById(id).map(categorieVehiculeMapper::toDomain);
  }

  @Override
  public boolean existeParId(String id) {
    return categorieVehiculeJpaRepository.existsById(id);
  }

  @Override
  public List<CategorieVehicule> lister() {
    return categorieVehiculeJpaRepository.findAll().stream()
        .map(categorieVehiculeMapper::toDomain)
        .toList();
  }

  @Override
  public Page<CategorieVehicule> lister(Pageable pageable) {
    var springPageRequest = PageMapper.toSpringPageRequest(pageable);
    var springPage = categorieVehiculeJpaRepository.findAll(springPageRequest);
    return PageMapper.fromSpringPage(springPage, categorieVehiculeMapper::toDomain);
  }

  @Override
  public boolean existeParCode(String code) {
    return categorieVehiculeJpaRepository.existsByCode(code);
  }

  @Override
  public Optional<CategorieVehicule> chercherParCode(String code) {
    return categorieVehiculeJpaRepository.findByCode(code)
        .map(categorieVehiculeMapper::toDomain);
  }
}
