package ci.komobe.actionelle.application.features.utilisateur.usecases;

import ci.komobe.actionelle.application.features.utilisateur.commands.ModifierUtilisateurCommand;
import ci.komobe.actionelle.domain.exceptions.UtilisateurErreur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

/**
 * Cas d'utilisation pour la modification d'un utilisateur
 *
 * @author Moro KONÉ 2025-06-01
 */
@RequiredArgsConstructor
public class ModifierUtilisateurUseCase {

  private final UtilisateurRepository utilisateurRepository;

  public void execute(ModifierUtilisateurCommand command) {

    var optionalUtilisateur = utilisateurRepository.chercherParUsername(command.getUsername());
    String utilisateurId = command.getId();
    optionalUtilisateur.ifPresent(utilisateur -> {
      if (!Objects.equals(utilisateur.getId(), utilisateurId)) {
        throw new UtilisateurErreur("le username est déjà utilisé par un autre utilisateur");
      }
    });

    var utilisateur = optionalUtilisateur
        .orElseGet(() -> utilisateurRepository.chercherParId(utilisateurId)
            .orElseThrow(() -> new UtilisateurErreur("Utilisateur non trouvé")));

    utilisateur.modifier(command.getUsername(), command.getRole());

    utilisateurRepository.enregistrer(utilisateur);
  }
}