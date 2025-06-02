package ci.komobe.actionelle.infrastructure.mappers;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.UtilisateurEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

  Utilisateur toDomain(UtilisateurEntity utilisateurJpa);

  @InheritInverseConfiguration
  UtilisateurEntity toEntity(Utilisateur utilisateur);
}
