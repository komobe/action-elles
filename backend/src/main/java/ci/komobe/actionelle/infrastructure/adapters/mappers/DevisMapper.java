package ci.komobe.actionelle.infrastructure.adapters.mappers;

import ci.komobe.actionelle.domain.entities.Devis;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.DevisJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Mapper(componentModel = "spring")
public interface DevisMapper {


  @Mapping(target = "categorie", ignore = true)
  @Mapping(target = "produit", ignore = true)
  @Mapping(target = "price", source = "prime")
  @Mapping(target = "vehicule.puissanceFiscale", source = "puissanceFiscale")
  @Mapping(target = "vehicule.dateMiseEnCirculation", source = "vehiculeDateMiseEnCirculation")
  @Mapping(target = "vehicule.valeurNeuf", source = "vehiculeValeurNeuf")
  @Mapping(target = "vehicule.valeurVenale", source = "vehiculeValeurVenale")
  DevisJpaEntity toEntity(Devis devis);

  @Mapping(target = "produitId", source = "produit.id")
  @Mapping(target = "categorieId", source = "categorie.id")
  @Mapping(target = "prime", source = "price")
  @Mapping(target = "vehiculeValeurVenale", source = "vehicule.valeurVenale")
  @Mapping(target = "vehiculeValeurNeuf", source = "vehicule.valeurNeuf")
  @Mapping(target = "vehiculeDateMiseEnCirculation", source = "vehicule.dateMiseEnCirculation")
  @Mapping(target = "puissanceFiscale", source = "vehicule.puissanceFiscale")
  Devis toDomain(DevisJpaEntity devisJpaEntity);
}
