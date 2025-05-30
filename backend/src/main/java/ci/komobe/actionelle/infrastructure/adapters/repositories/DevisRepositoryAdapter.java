package ci.komobe.actionelle.infrastructure.adapters.repositories;

import ci.komobe.actionelle.application.repositories.DevisRepository;
import ci.komobe.actionelle.domain.entities.Devis;
import ci.komobe.actionelle.infrastructure.adapters.mappers.DevisMapper;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.CategorieVehiculeEntityJpa;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.ProduitEntityJpa;
import ci.komobe.actionelle.infrastructure.hibernatejpa.repositories.DevisJpaRepository;
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
    var categorieVehiculeEntityJpa = new CategorieVehiculeEntityJpa();
    categorieVehiculeEntityJpa.setId(devis.getCategorieId());
    var produitEntityJpa = new ProduitEntityJpa();
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
