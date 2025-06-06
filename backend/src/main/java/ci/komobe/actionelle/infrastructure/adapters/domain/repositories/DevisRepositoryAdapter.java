package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.domain.entities.Devis;
import ci.komobe.actionelle.domain.repositories.DevisRepository;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import ci.komobe.actionelle.infrastructure.mappers.DevisMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.ProduitEntity;
import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.VehiculeEntity;
import ci.komobe.actionelle.infrastructure.persistences.jpa.mappers.PageMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.repositories.DevisJpaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Repository
@Transactional
@AllArgsConstructor
public class DevisRepositoryAdapter implements DevisRepository {

  private final DevisJpaRepository devisJpaRepository;
  private final DevisMapper devisMapper;

  @Override
  public void enregistrer(Devis devis) {
    var devisJpaEntity = devisMapper.toEntity(devis);

    var produitEntityJpa = new ProduitEntity();
    produitEntityJpa.setId(devis.getProduitId());
    devisJpaEntity.setProduit(produitEntityJpa);

    var vehicule = new VehiculeEntity();
    vehicule.setId(devis.getVehiculeId());
    devisJpaEntity.setVehicule(vehicule);

    devisJpaRepository.save(devisJpaEntity);
  }

  @Override
  public void supprimer(Devis entity) {
    devisJpaRepository.delete(devisMapper.toEntity(entity));
  }

  @Override
  public Optional<Devis> chercherParId(String id) {
    return devisJpaRepository.findById(id).map(devisMapper::toDomain);
  }

  @Override
  public boolean existeParId(String id) {
    return devisJpaRepository.existsById(id);
  }

  @Override
  public List<Devis> lister() {
    return devisJpaRepository.findAll().stream().map(devisMapper::toDomain).toList();
  }

  @Override
  public Page<Devis> lister(Pageable pageable) {
    var springPageRequest = PageMapper.toSpringPageRequest(pageable);
    var springPage = devisJpaRepository.findAll(springPageRequest);
    return PageMapper.fromSpringPage(springPage, devisMapper::toDomain);
  }

  @Override
  public boolean existeParReference(String reference) {
    return devisJpaRepository.existsByReference(reference);
  }

  @Override
  public Optional<Devis> chercherParReference(String reference) {
    return devisJpaRepository.findByReference(reference)
        .map(devisMapper::toDomain);
  }
}
