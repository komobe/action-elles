package ci.komobe.actionelle.application.features.utilisateur.usecases;

import ci.komobe.actionelle.application.commons.providers.PasswordProvider;
import ci.komobe.actionelle.application.features.utilisateur.commands.ModifierMotPasseCommand;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.exceptions.UtilisateurErreur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author Moro KONÉ 2025-06-03
 */
@RequiredArgsConstructor
public class ModifierMotPasseUseCase {

  private final UtilisateurRepository utilisateurRepository;
  private final PasswordProvider passwordProvider;

  public void execute(ModifierMotPasseCommand command) {
    Utilisateur utilisateur = utilisateurRepository.chercherParId(command.getId())
        .orElseThrow(() -> new UtilisateurErreur("Utilisateur non trouvé"));

    String newPassword = command.getNewPassword();
    String currentPassword = utilisateur.getPassword();

    boolean matches = passwordProvider.matches(newPassword, currentPassword);
    if (!matches) {
      throw new UtilisateurErreur("Vous utilisez déjà ce mot de passe");
    }

    String nouveauMotPasseEncoder = passwordProvider.encode(newPassword);

    utilisateur.modifieMotPasse(nouveauMotPasseEncoder);

    utilisateurRepository.enregistrer(utilisateur);
  }
}
