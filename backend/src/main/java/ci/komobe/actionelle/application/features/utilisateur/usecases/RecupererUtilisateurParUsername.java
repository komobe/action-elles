package ci.komobe.actionelle.application.features.utilisateur.usecases;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.exceptions.UtilisateurErreur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-31
 */
@AllArgsConstructor
public class RecupererUtilisateurParUsername {

  private final UtilisateurRepository utilisateurRepository;

  public Utilisateur executer(String username) {
    return utilisateurRepository.chercherParUsername(username)
        .orElseThrow(() -> new UtilisateurErreur("Utilisateur inconnu"));
  }
}
