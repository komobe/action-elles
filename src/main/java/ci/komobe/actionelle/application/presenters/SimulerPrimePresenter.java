package ci.komobe.actionelle.application.presenters;

import java.math.BigDecimal;

/**
 * @author Moro KONÉ 2025-05-29
 */
public interface SimulerPrimePresenter<T> {

  void addData(BigDecimal data);

  T present();
}
