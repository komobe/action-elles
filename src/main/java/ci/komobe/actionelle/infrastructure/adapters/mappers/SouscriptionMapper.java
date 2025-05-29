package ci.komobe.actionelle.infrastructure.adapters.mappers;

import org.mapstruct.Mapper;

import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.SouscriptionEntityJpa;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface SouscriptionMapper {

  Souscription toDomain(SouscriptionEntityJpa souscriptionEntity);

  SouscriptionEntityJpa toEntity(Souscription souscription);
}
