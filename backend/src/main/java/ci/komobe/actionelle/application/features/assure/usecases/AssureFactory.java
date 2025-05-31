package ci.komobe.actionelle.application.features.assure.usecases;

import ci.komobe.actionelle.application.features.assure.commands.AssureCommandBase;
import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.utils.IdGenerator;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class AssureFactory {

  private AssureFactory() {
  }

  public static Assure factory(AssureCommandBase command) {
    return Assure.builder()
        .id(IdGenerator.generateId())
        .numeroCarteIdentite(command.getNumeroCarteIdentite())
        .nom(command.getNom())
        .prenom(command.getPrenom())
        .adresse(command.getAdresse())
        .telephone(command.getTelephone())
        .ville(command.getVille())
        .build();
  }
}
