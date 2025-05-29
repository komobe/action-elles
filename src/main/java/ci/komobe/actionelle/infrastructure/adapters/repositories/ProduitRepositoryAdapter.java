package ci.komobe.actionelle.infrastructure.adapters.repositories;

import ci.komobe.actionelle.application.repositories.ProduitRepository;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.infrastructure.adapters.mappers.ProduitMapper;
import ci.komobe.actionelle.infrastructure.hibernatejpa.repositories.ProduitJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Repository
public class ProduitRepositoryAdapter implements ProduitRepository {

  private final ProduitMapper produitMapper;
  private final ProduitJpaRepository produitJpaRepository;

  public ProduitRepositoryAdapter(
      ProduitJpaRepository produitJpaRepository,
      ProduitMapper produitMapper
  ) {
    this.produitMapper = produitMapper;
    this.produitJpaRepository = produitJpaRepository;
  }

  @Override
  public Optional<Produit> findByNom(String code) {
    return produitJpaRepository.findByNom(code).map(produitMapper::toDomain);
  }

  @Override
  public List<Produit> findAll() {
    return produitJpaRepository.findAll().stream().map(produitMapper::toDomain).toList();
  }

  @Override
  public void save(Produit produit) {
    var produitEntity = produitMapper.toEntity(produit);
    produitJpaRepository.save(produitEntity);
  }

  @Override
  public boolean existsByNom(String code) {
    return produitJpaRepository.existsByNom(code);
  }
}
