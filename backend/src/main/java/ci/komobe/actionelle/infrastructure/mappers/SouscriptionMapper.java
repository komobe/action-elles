package ci.komobe.actionelle.infrastructure.mappers;

import org.mapstruct.Mapper;

import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.SouscriptionEntity;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface SouscriptionMapper {

  Souscription toDomain(SouscriptionEntity souscriptionEntity);

  SouscriptionEntity toEntity(Souscription souscription);
}
