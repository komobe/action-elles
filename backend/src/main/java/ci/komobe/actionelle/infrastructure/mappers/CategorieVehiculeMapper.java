package ci.komobe.actionelle.infrastructure.mappers;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.CategorieVehiculeEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface CategorieVehiculeMapper {

  CategorieVehicule toDomain(CategorieVehiculeEntity categorieVehiculeEntity);

  @Mapping(target = "vehicules", ignore = true)
  @Mapping(target = "produits", ignore = true)
  @Named("mapToCategorieEntity")
  @InheritInverseConfiguration
  CategorieVehiculeEntity toEntity(CategorieVehicule categorieVehicule);
}

