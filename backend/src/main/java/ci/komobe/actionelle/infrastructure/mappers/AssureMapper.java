package ci.komobe.actionelle.infrastructure.mappers;

import org.mapstruct.Mapper;

import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.AssureEntity;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface AssureMapper {

  Assure toDomain(AssureEntity assureEntity);

  @Mapping(target = "souscriptions", ignore = true)
  AssureEntity toEntity(Assure assure);
}
