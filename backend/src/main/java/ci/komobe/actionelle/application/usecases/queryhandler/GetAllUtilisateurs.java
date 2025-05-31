package ci.komobe.actionelle.application.usecases.queryhandler;

import ci.komobe.actionelle.application.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import java.util.Collection;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-31
 */
@AllArgsConstructor
public class GetAllUtilisateurs {

  private final UtilisateurRepository utilisateurRepository;

  public Collection<Utilisateur> get() {
    return utilisateurRepository.findAll();
  }
}
