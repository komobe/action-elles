package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.domain.repositories.DevisRepository;
import ci.komobe.actionelle.domain.entities.Devis;
import ci.komobe.actionelle.infrastructure.mappers.DevisMapper;
import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.CategorieVehiculeEntity;
import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.ProduitEntity;
import ci.komobe.actionelle.infrastructure.persistences.postgres.repositories.DevisJpaRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Repository
@AllArgsConstructor
public class DevisRepositoryAdapter implements DevisRepository {

  private final DevisJpaRepository devisJpaRepository;
  private final DevisMapper devisMapper;

  @Override
  public void save(Devis devis) {
    var devisJpaEntity = devisMapper.toEntity(devis);
    var categorieVehiculeEntityJpa = new CategorieVehiculeEntity();
    categorieVehiculeEntityJpa.setId(devis.getCategorieId());
    var produitEntityJpa = new ProduitEntity();
    produitEntityJpa.setId(devis.getProduitId());
    devisJpaEntity.setProduit(produitEntityJpa);
    devisJpaEntity.setCategorie(categorieVehiculeEntityJpa);
    devisJpaRepository.save(devisJpaEntity);
  }

  @Override
  public boolean existsByReference(String reference) {
    return devisJpaRepository.existsByReference(reference);
  }

  @Override
  public Optional<Devis> findByReference(String reference) {
    return devisJpaRepository.findByReference(reference)
        .map(devisMapper::toDomain);
  }
}
