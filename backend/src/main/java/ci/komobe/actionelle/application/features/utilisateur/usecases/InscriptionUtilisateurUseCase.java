package ci.komobe.actionelle.application.features.utilisateur.usecases;

import ci.komobe.actionelle.application.features.utilisateur.commands.InscriptionUtilisateurCommand;
import ci.komobe.actionelle.application.features.utilisateur.UtilisateurError;
import ci.komobe.actionelle.application.commons.providers.PasswordProvider;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.utils.IdGenerator;

/**
 * @author Moro KONÉ 2025-05-30
 */
public class InscriptionUtilisateurUseCase {

  private final UtilisateurRepository utilisateurRepository;
  private final PasswordProvider passwordProvider;

  public InscriptionUtilisateurUseCase(
      UtilisateurRepository utilisateurRepository,
      PasswordProvider passwordProvider) {
    this.utilisateurRepository = utilisateurRepository;
    this.passwordProvider = passwordProvider;
  }

  public void execute(InscriptionUtilisateurCommand command) {
    var username = command.username().trim();
    var password = command.password().trim();

    if (utilisateurRepository.existsByUsername(username)) {
      throw new UtilisateurError("Utilisateur déjà existant");
    }

    var encodedPassword = passwordProvider.encode(password);

    var utilisateur = new Utilisateur();
    utilisateur.setId(IdGenerator.generateId());
    utilisateur.inscrire(username, encodedPassword);

    utilisateurRepository.save(utilisateur);
  }
}
