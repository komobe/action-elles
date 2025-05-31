package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.infrastructure.mappers.CategorieVehiculeMapper;
import ci.komobe.actionelle.infrastructure.persistences.postgres.repositories.CategorieVehiculeJpaRepository;
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
  public void save(CategorieVehicule categorieVehicule) {
    var categorieVehiculeEntity = categorieVehiculeMapper.toEntity(categorieVehicule);
    categorieVehiculeJpaRepository.save(categorieVehiculeEntity);
  }

  @Override
  public List<CategorieVehicule> findAll() {
    return categorieVehiculeJpaRepository.findAll().stream()
        .map(categorieVehiculeMapper::toDomain)
        .toList();
  }

  @Override
  public boolean existsByCode(String code) {
    return categorieVehiculeJpaRepository.existsByCode(code);
  }

  @Override
  public Optional<CategorieVehicule> findByCode(String code) {
    return categorieVehiculeJpaRepository.findByCode(code)
        .map(categorieVehiculeMapper::toDomain);
  }
}
