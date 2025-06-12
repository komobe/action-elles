package ci.komobe.actionelle.application.features.utilisateur.usecases;

import ci.komobe.actionelle.application.commons.providers.PasswordProvider;
import ci.komobe.actionelle.application.features.utilisateur.commands.InscriptionUtilisateurCommand;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.exceptions.UtilisateurErreur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * @author Moro KONÉ 2025-05-30
 */
@RequiredArgsConstructor
public class InscrireUtilisateur {

  private final UtilisateurRepository utilisateurRepository;
  private final PasswordProvider passwordProvider;

  public void executer(InscriptionUtilisateurCommand command) {
    if (command.username() == null || !StringUtils.hasText(command.username())) {
      throw new UtilisateurErreur("Le username ne peut pas être vide");
    }

    if (command.password() == null || !StringUtils.hasText(command.password())) {
      throw new UtilisateurErreur("Le mot de passe ne peut pas être vide");
    }

    var username = command.username().trim();
    var password = command.password().trim();

    if (utilisateurRepository.existParUsername(username)) {
      throw new UtilisateurErreur("Un utilisateur avec le username " + username + " existe déjà");
    }

    var encodedPassword = passwordProvider.encode(password);

    var utilisateur = new Utilisateur();
    utilisateur.setId(IdGenerator.generateId());
    utilisateur.inscrire(username, encodedPassword);

    utilisateurRepository.enregistrer(utilisateur);
  }
}
