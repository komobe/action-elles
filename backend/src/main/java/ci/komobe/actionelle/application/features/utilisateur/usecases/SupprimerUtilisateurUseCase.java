package ci.komobe.actionelle.application.features.utilisateur.usecases;

import ci.komobe.actionelle.domain.exceptions.UtilisateurErreur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;

/**
 * Cas d'utilisation pour la suppression d'un utilisateur
 * 
 * @author Moro KONÉ 2025-06-01
 */
@RequiredArgsConstructor
public class SupprimerUtilisateurUseCase {

  private final UtilisateurRepository utilisateurRepository;

  public void execute(String id) {
    var utilisateur = utilisateurRepository.chercherParId(id)
        .orElseThrow(() -> new UtilisateurErreur("Utilisateur non trouvé"));

    utilisateurRepository.supprimer(utilisateur);
  }
}