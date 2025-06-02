package ci.komobe.actionelle.infrastructure.mappers;

import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.VehiculeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring", uses = { CategorieVehiculeMapper.class })
public abstract class VehiculeMapper {

  @Mapping(target = "valeurNeuf", expression = "java(entity.getValeurNeuf())")
  public abstract Vehicule toDomain(VehiculeEntity entity);

  @Mapping(target = "categorie", qualifiedByName = "mapToCategorieEntity")
  @Mapping(target = "souscriptions", ignore = true)
  @Mapping(target = "valeurNeuf", expression = "java(dto.getValeurNeuf())")
  @Mapping(target = "deviseMontant", ignore = true)
  public abstract VehiculeEntity toEntity(Vehicule dto);
}
