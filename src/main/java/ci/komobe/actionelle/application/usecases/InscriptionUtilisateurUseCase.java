package ci.komobe.actionelle.application.usecases;

import ci.komobe.actionelle.application.commands.InscriptionUtilisateurCommand;
import ci.komobe.actionelle.application.exceptions.UtilisateurError;
import ci.komobe.actionelle.application.providers.PasswordProvider;
import ci.komobe.actionelle.application.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.entities.Utilisateur;

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
    utilisateur.inscrire(username, encodedPassword);

    utilisateurRepository.save(utilisateur);
  }
}
