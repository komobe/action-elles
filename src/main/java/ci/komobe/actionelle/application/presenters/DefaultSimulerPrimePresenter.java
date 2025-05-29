package ci.komobe.actionelle.application.presenters;

import java.math.BigDecimal;

/**
 * @author Moro KONÉ 2025-05-29
 */
public class DefaultSimulerPrimePresenter implements SimulerPrimePresenter<BigDecimal> {

  private BigDecimal valeur = BigDecimal.ZERO;

  @Override
  public void addData(BigDecimal data) {
    valeur = data;
  }

  @Override
  public BigDecimal present() {
    return valeur;
  }
}
