package ci.komobe.actionelle.infrastructure.mappers;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.CategorieVehiculeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface CategorieVehiculeMapper {

  CategorieVehicule toDomain(CategorieVehiculeEntity categorieVehiculeEntity);

  @Mapping(target = "vehicules", ignore = true)
  @Mapping(target = "produits", ignore = true)
  @Mapping(target = "simulationsDevis", ignore = true)
  CategorieVehiculeEntity toEntity(CategorieVehicule categorieVehicule);
}
