package ci.komobe.actionelle.application.features.devis.presenters;

import ci.komobe.actionelle.application.features.devis.dto.SimulationPrimeResult;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface SimulerPrimePresenter<T> {

  void addData(SimulationPrimeResult data);

  T present();
}
