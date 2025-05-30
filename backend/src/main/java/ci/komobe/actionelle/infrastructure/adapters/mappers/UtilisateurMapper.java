package ci.komobe.actionelle.infrastructure.adapters.mappers;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.UtilisateurJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

  Utilisateur toDomain(UtilisateurJpaEntity utilisateurJpa);

  @Mapping(target = "authorities", ignore = true)
  UtilisateurJpaEntity toEntity(Utilisateur utilisateur);
}
