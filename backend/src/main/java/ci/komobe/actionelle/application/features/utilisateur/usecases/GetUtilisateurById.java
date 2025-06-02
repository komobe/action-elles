package ci.komobe.actionelle.application.features.utilisateur.usecases;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-31
 */
@AllArgsConstructor
public class GetUtilisateurById {

  private final UtilisateurRepository utilisateurRepository;

  public Optional<Utilisateur> get(String utilisateurId) {
    return utilisateurRepository.chercherParId(utilisateurId);
  }
}
