package ci.komobe.actionelle.infrastructure.adapters.mappers;

import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.ProduitEntityJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface ProduitMapper {

  Produit toDomain(ProduitEntityJpa produitEntity);

  @Mapping(target = "garanties", ignore = true)
  ProduitEntityJpa toEntity(Produit produit);
}
