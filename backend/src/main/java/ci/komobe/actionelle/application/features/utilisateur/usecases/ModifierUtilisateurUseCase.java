package ci.komobe.actionelle.application.features.utilisateur.usecases;

import ci.komobe.actionelle.domain.exceptions.UtilisateurErreur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.valueobjects.Role;
import lombok.RequiredArgsConstructor;

/**
 * Cas d'utilisation pour la modification d'un utilisateur
 * 
 * @author Moro KONÉ 2025-06-01
 */
@RequiredArgsConstructor
public class ModifierUtilisateurUseCase {

  private final UtilisateurRepository utilisateurRepository;

  public void execute(String id, Role role) {
    var utilisateur = utilisateurRepository.chercherParId(id)
        .orElseThrow(() -> new UtilisateurErreur("Utilisateur non trouvé"));

    utilisateur.setRole(role);
    utilisateurRepository.enregistrer(utilisateur);
  }
}