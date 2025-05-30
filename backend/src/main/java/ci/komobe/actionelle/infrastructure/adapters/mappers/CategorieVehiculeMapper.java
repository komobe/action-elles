package ci.komobe.actionelle.infrastructure.adapters.mappers;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.CategorieVehiculeEntityJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface CategorieVehiculeMapper {

  CategorieVehicule toDomain(CategorieVehiculeEntityJpa categorieVehiculeEntity);

  @Mapping(target = "vehicules", ignore = true)
  @Mapping(target = "produits", ignore = true)
  @Mapping(target = "simulationsDevis", ignore = true)
  CategorieVehiculeEntityJpa toEntity(CategorieVehicule categorieVehicule);
}
