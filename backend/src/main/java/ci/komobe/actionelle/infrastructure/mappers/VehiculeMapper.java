package ci.komobe.actionelle.infrastructure.mappers;

import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.VehiculeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface VehiculeMapper {

  Vehicule toDomain(VehiculeEntity entity);

  @Mapping(target = "souscriptions", ignore = true)
  VehiculeEntity toEntity(Vehicule dto);
}
