package ci.komobe.actionelle.domain.valueobjects;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Représente une valeur monétaire. Pour la persistance, le montant et la devise sont stockés
 * séparément.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Valeur {

  private static final Locale LOCALE_FR = Locale.of("fr", "CI");
  private final BigDecimal montant;
  private final String devise;

  /**
   * Crée une nouvelle valeur à partir d'un montant et d'une devise.
   *
   * @param montant Le montant
   * @param devise  La devise (ex: XOF, EUR)
   * @return Une nouvelle instance de Valeur
   * @throws IllegalArgumentException si les paramètres sont invalides
   */
  public static Valeur of(BigDecimal montant, String devise) {
    if (montant == null) {
      throw new IllegalArgumentException("Le montant ne peut pas être null");
    }
    if (montant.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Le montant ne peut pas être négatif");
    }
    if (devise == null || devise.trim().isEmpty()) {
      throw new IllegalArgumentException("La devise est obligatoire");
    }
    return new Valeur(montant, devise.toUpperCase());
  }

  /**
   * Crée une nouvelle valeur à partir d'un montant en double et d'une devise.
   */
  public static Valeur of(double montant, String devise) {
    return of(BigDecimal.valueOf(montant), devise);
  }

  /**
   * Crée une valeur de zéro dans la devise spécifiée.
   */
  public static Valeur zero(String devise) {
    return of(BigDecimal.ZERO, devise);
  }

  /**
   * Convertit la Valeur en chaîne de caractères. Format: "1 000 000 XOF"
   *
   * @return la chaîne formatée avec séparateurs de milliers
   */
  @Override
  public String toString() {
    NumberFormat format = NumberFormat.getInstance(LOCALE_FR);
    return format.format(montant) + " " + devise;
  }

  /**
   * Compare l'égalité avec une autre Valeur. Deux valeurs sont égales si elles ont le même montant
   * et la même devise.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Valeur other)) {
      return false;
    }
    return this.montant.compareTo(other.montant) == 0 &&
        this.devise.equalsIgnoreCase(other.devise);
  }

  @Override
  public int hashCode() {
    return Objects.hash(montant, devise.toUpperCase());
  }
}
