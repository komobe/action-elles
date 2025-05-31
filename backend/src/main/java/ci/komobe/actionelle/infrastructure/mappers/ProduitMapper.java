package ci.komobe.actionelle.infrastructure.mappers;

import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.ProduitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface ProduitMapper {

  Produit toDomain(ProduitEntity produitEntity);

  @Mapping(target = "garanties", ignore = true)
  ProduitEntity toEntity(Produit produit);
}
