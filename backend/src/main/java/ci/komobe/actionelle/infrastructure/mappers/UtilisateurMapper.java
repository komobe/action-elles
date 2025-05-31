package ci.komobe.actionelle.infrastructure.mappers;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.UtilisateurEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

  Utilisateur toDomain(UtilisateurEntity utilisateurJpa);

  @Mapping(target = "authorities", ignore = true)
  UtilisateurEntity toEntity(Utilisateur utilisateur);
}
