package ci.komobe.actionelle.infrastructure.adapters.mappers;

import org.mapstruct.Mapper;

import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.AssureEntityJpa;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface AssureMapper {

  Assure toDomain(AssureEntityJpa assureEntity);

  @Mapping(target = "souscriptions", ignore = true)
  AssureEntityJpa toEntity(Assure assure);
}
