package ci.komobe.actionelle.application.features.utilisateur.usecases;

import ci.komobe.actionelle.application.commons.providers.PasswordProvider;
import ci.komobe.actionelle.application.features.utilisateur.commands.ModifierMotPasseCommand;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.exceptions.UtilisateurErreur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;

/**
 * Use Case pour modifier le mot de passe d'un utilisateur
 *
 * @author Moro KONÉ 2025-06-03
 */
@RequiredArgsConstructor
public class ModifierMotPasseUseCase {

  private final UtilisateurRepository utilisateurRepository;
  private final PasswordProvider passwordProvider;

  public void execute(ModifierMotPasseCommand command) {
    Utilisateur utilisateur = recupererUtilisateur(command.getId());
    String nouveauMotPasse = command.getNewPassword();
    verifierMotPasseDifferent(nouveauMotPasse, utilisateur.getPassword());
    String nouveauMotPasseCrypte = crypterMotPasse(nouveauMotPasse);
    utilisateur.modifieMotPasse(nouveauMotPasse, nouveauMotPasseCrypte);
    utilisateurRepository.enregistrer(utilisateur);
  }

  private Utilisateur recupererUtilisateur(String id) {
    return utilisateurRepository.chercherParId(id)
        .orElseThrow(() -> new UtilisateurErreur("Utilisateur non trouvé"));
  }

  private void verifierMotPasseDifferent(String nouveauMotPasse, String ancienMotPasse) {
    if (motPasseIdentique(nouveauMotPasse, ancienMotPasse)) {
      throw new UtilisateurErreur("Vous utilisez déjà ce mot de passe");
    }
  }

  private String crypterMotPasse(String motPasse) {
    return passwordProvider.encode(motPasse);
  }

  private boolean motPasseIdentique(String nouveauMotPasse, String ancienMotPasseEncode) {
    return passwordProvider.matches(nouveauMotPasse, ancienMotPasseEncode);
  }
}

