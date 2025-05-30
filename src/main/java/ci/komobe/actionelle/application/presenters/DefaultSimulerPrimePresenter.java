package ci.komobe.actionelle.application.presenters;

import ci.komobe.actionelle.application.valueobjects.SimulationPrimeResult;

/**
 * @author Moro KONÉ 2025-05-29
 */
public class DefaultSimulerPrimePresenter implements SimulerPrimePresenter<SimulationPrimeResult> {

  private SimulationPrimeResult primeResult;

  @Override
  public void addData(SimulationPrimeResult data) {
    primeResult = data;
  }

  @Override
  public SimulationPrimeResult present() {
    return primeResult;
  }
}
