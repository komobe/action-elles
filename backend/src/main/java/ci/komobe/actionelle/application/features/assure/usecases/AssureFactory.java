package ci.komobe.actionelle.application.features.assure.usecases;

import ci.komobe.actionelle.application.features.assure.commands.CreerAssureCommand;
import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.utils.IdGenerator;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class AssureFactory {

  private AssureFactory() {
  }

  public static Assure factory(CreerAssureCommand command) {
    return Assure.builder()
        .id(IdGenerator.generateId())
        .numeroCarteIdentite(command.getNumeroCarteIdentite())
        .nom(command.getNom())
        .prenoms(command.getPrenoms())
        .dateNaissance(command.getDateNaissance())
        .lieuNaissance(command.getLieuNaissance())
        .email(command.getEmail())
        .adresse(command.getAdresse())
        .telephone(command.getTelephone())
        .build();
  }
}
