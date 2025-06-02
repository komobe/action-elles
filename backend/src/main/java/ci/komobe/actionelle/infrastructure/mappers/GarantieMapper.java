package ci.komobe.actionelle.infrastructure.mappers;

import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.GarantieEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(componentModel = "spring")
public interface GarantieMapper {

  Garantie toDomain(GarantieEntity garantieEntity);

  @Mapping(target = "produits", ignore = true)
  @InheritInverseConfiguration
  GarantieEntity toEntity(Garantie garantie);
}
