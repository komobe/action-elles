package ci.komobe.actionelle.infrastructure.mappers;

import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.SouscriptionEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Mapper(
    componentModel = "spring",
    uses = {VehiculeMapper.class, ProduitMapper.class, AssureMapper.class}
)
public interface SouscriptionMapper {

  Souscription toDomain(SouscriptionEntity souscriptionEntity);

  @InheritInverseConfiguration
  SouscriptionEntity toEntity(Souscription souscription);
}
