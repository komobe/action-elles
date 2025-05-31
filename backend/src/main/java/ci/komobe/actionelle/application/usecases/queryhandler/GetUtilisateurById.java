package ci.komobe.actionelle.application.usecases.queryhandler;

import ci.komobe.actionelle.application.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import java.util.Optional;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-31
 */
@AllArgsConstructor
public class GetUtilisateurById {

  private final UtilisateurRepository utilisateurRepository;

  public Optional<Utilisateur> get(String utilisateurId) {
    return utilisateurRepository.findById(utilisateurId);
  }
}
