package ci.komobe.actionelle.infrastructure.mappers;

import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.ProduitEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring", uses = { GarantieMapper.class, CategorieVehiculeMapper.class })
public interface ProduitMapper {

  @InheritInverseConfiguration
  Produit toDomain(ProduitEntity produitEntity);

  @Mapping(target = "souscriptions", ignore = true)
  @Mapping(target = "simulationsDevis", ignore = true)
  ProduitEntity toEntity(Produit produit);
}
