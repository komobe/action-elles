package ci.komobe.actionelle.application.usecases.queryhandler;

import ci.komobe.actionelle.application.exceptions.UtilisateurError;
import ci.komobe.actionelle.application.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-31
 */
@AllArgsConstructor
public class GetUtilisateurByUsername {

  private final UtilisateurRepository utilisateurRepository;

  public Utilisateur get(String username) {
    return utilisateurRepository.findByUsername(username)
        .orElseThrow(() -> new UtilisateurError("Utilisateur inconnu"));
  }
}
