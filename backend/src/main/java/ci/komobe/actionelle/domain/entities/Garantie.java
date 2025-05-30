package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.valueobjects.Prime;
import ci.komobe.actionelle.domain.valueobjects.PuissanceFiscale;
import ci.komobe.actionelle.domain.valueobjects.TypeBaseCalcul;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-28
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = { "id" })
public class Garantie {

  private String id;
  private String libelle;
  private String description;
  private String code;
  private PuissanceFiscale puissanceFiscale;
  private TypeBaseCalcul baseDeCalcul;
  private Prime prime;
  private Prime primeMinimum;
  private int maxAge;
  private boolean plafonne;

  public boolean estSuperieurMaxAge(double age) {
    return maxAge > 0 && age > maxAge;
  }

  public boolean isPrimeMinimum() {
    return primeMinimum != null;
  }
}
