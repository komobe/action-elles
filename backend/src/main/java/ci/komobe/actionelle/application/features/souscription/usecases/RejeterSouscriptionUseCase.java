package ci.komobe.actionelle.application.features.souscription.usecases;

import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.exceptions.SouscriptionErreur;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;

/**
 * @author Moro KONÉ 2025-06-02
 */
public record RejeterSouscriptionUseCase(
    SouscriptionRepository souscriptionRepository) {
  public void execute(String souscriptionId) {
    Souscription souscription = souscriptionRepository.chercherParId(souscriptionId)
        .orElseThrow(() -> new SouscriptionErreur("La souscription " + souscriptionId + " n'existe pas"));
    souscription.rejeter();
    souscriptionRepository.enregistrer(souscription);
  }
}
