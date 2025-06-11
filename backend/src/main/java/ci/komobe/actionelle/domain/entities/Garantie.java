package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.exceptions.DomainErreur;
import ci.komobe.actionelle.domain.valueobjects.Prime;
import ci.komobe.actionelle.domain.valueobjects.PuissanceFiscale;
import ci.komobe.actionelle.domain.valueobjects.TypeBaseCalcul;
import java.math.BigDecimal;
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
@EqualsAndHashCode(of = {"id"})
public class Garantie {

  private static final BigDecimal TAUX_CALCUL_VALEUR_ASSUREE = BigDecimal.valueOf(0.5);

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

  public boolean estEligible(double age) {
    return !estSuperieurMaxAge(age);
  }

  public boolean isPrimeMinimum() {
    return primeMinimum != null;
  }

  public boolean estNonPlafonne() {
    return !plafonne;
  }


  /**
   * Applique le plafonnement si nécessaire selon les règles métier de la garantie. Logique métier :
   * le montant est plafonné à la valeur assurée si la garantie est plafonnée.
   *
   * @return Le montant final après application éventuelle du plafonnement
   */
  public BigDecimal appliquerPlafonnement(BigDecimal montant, BigDecimal valeurVenale) {

    if (montant == null || valeurVenale == null) {
      throw new DomainErreur("Le montant et la valeur vénale ne peuvent pas être null");
    }

    if (estNonPlafonne() || montant.compareTo(BigDecimal.ZERO) <= 0) {
      return montant;
    }

    BigDecimal valeurAssuree = calculerValeurAssuree(valeurVenale);

    return montant.min(valeurAssuree);
  }

  /**
   * Calcule la valeur assurée selon les règles métier. Règle : 50% de la valeur vénale.
   *
   * @return La valeur assurée
   */
  public BigDecimal calculerValeurAssuree(BigDecimal valeurVenale) {
    if (valeurVenale == null) {
      throw new DomainErreur("La valeur vénale ne peut pas être null");
    }

    return valeurVenale.multiply(TAUX_CALCUL_VALEUR_ASSUREE);
  }
}
