package ci.komobe.actionelle.infrastructure.adapters.domain.repositories;

import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.Pageable;
import ci.komobe.actionelle.infrastructure.mappers.ProduitMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.mappers.PageMapper;
import ci.komobe.actionelle.infrastructure.persistences.jpa.repositories.ProduitJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Repository
@AllArgsConstructor
public class ProduitRepositoryAdapter implements ProduitRepository {

  private final ProduitMapper produitMapper;
  private final ProduitJpaRepository produitJpaRepository;

  @Override
  public Optional<Produit> chercherParNom(String nom) {
    return produitJpaRepository.findByNom(nom).map(produitMapper::toDomain);
  }

  @Override
  public boolean existeParCode(String code) {
    return produitJpaRepository.existsByCode(code);
  }

  @Override
  public List<Produit> lister() {
    return produitJpaRepository.findAll().stream()
        .map(produitMapper::toDomain).toList();
  }

  @Override
  public Page<Produit> lister(Pageable pageable) {
    var springPageRequest = PageMapper.toSpringPageRequest(pageable);
    var springPage = produitJpaRepository.findAll(springPageRequest);
    return PageMapper.fromSpringPage(springPage, produitMapper::toDomain);
  }

  @Override
  public void enregistrer(Produit produit) {
    var produitEntity = produitMapper.toEntity(produit);
    produitJpaRepository.save(produitEntity);
  }

  @Override
  public void supprimer(Produit entity) {
    produitJpaRepository.delete(produitMapper.toEntity(entity));
  }

  @Override
  public Optional<Produit> chercherParId(String id) {
    return produitJpaRepository.findById(id).map(produitMapper::toDomain);
  }

  @Override
  public boolean existeParId(String id) {
    return false;
  }

  @Override
  public boolean existParNom(String nom) {
    return produitJpaRepository.existsByNom(nom);
  }
}
