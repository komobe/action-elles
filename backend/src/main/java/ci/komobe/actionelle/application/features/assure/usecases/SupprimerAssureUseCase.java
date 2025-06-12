package ci.komobe.actionelle.application.features.assure.usecases;

import ci.komobe.actionelle.domain.exceptions.AssureErreur;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SupprimerAssureUseCase {

  private final AssureRepository assureRepository;

  public void execute(String id) {
    var assure = assureRepository.chercherParId(id)
        .orElseThrow(() -> new AssureErreur("Assuré non trouvé"));

    assureRepository.supprimer(assure);
  }
}