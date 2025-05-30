package ci.komobe.actionelle.infrastructure.adapters.mappers;

import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.GarantieEntityJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface GarantieMapper {

  Garantie toDomain(GarantieEntityJpa garantieEntity);

  @Mapping(target = "produits", ignore = true)
  GarantieEntityJpa toEntity(Garantie garantie);
}
