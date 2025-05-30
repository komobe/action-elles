package ci.komobe.actionelle.infrastructure.adapters.mappers;

import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.VehiculeEntityJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface VehiculeMapper {

  Vehicule toDomain(VehiculeEntityJpa entity);

  @Mapping(target = "souscriptions", ignore = true)
  VehiculeEntityJpa toEntity(Vehicule dto);
}
