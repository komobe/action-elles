package ci.komobe.actionelle.application.features.utilisateur.usecases;

import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.PageRequest;
import lombok.AllArgsConstructor;

/**
 * @author Moro KONÉ 2025-05-31
 */
@AllArgsConstructor
public class ListerUtilisateurs {

  private final UtilisateurRepository utilisateurRepository;

  public Page<Utilisateur> executer(PageRequest pageRequest) {
    return utilisateurRepository.findAll(pageRequest);
  }
}
