package ci.komobe.actionelle.infrastructure.mappers;

import ci.komobe.actionelle.domain.entities.Devis;
import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.DevisEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Mapper(componentModel = "spring")
public interface DevisMapper {

  @Mapping(target = "produitId", source = "produit.id")
  @Mapping(target = "vehiculeId", source = "vehicule.id")
  @Mapping(target = "montantPrime", source = "price")
  Devis toDomain(DevisEntity devisEntity);

  @InheritInverseConfiguration
  DevisEntity toEntity(Devis devis);
}
