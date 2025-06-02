package ci.komobe.actionelle.domain.valueobjects;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public record PuissanceFiscale(Integer debut, Integer fin) {

  public PuissanceFiscale {
    if (debut == null && fin == null) {
      throw new IllegalArgumentException("Début et fin ne peuvent pas être tous les deux null");
    }
    if (debut != null && fin != null && debut > fin) {
      throw new IllegalArgumentException("Début ne peut pas être supérieur à fin");
    }
  }

  public static PuissanceFiscale of(Integer debut, Integer fin) {
    return new PuissanceFiscale(debut, fin);
  }

  public static PuissanceFiscale fromDebut(Integer debut) {
    return new PuissanceFiscale(debut, null);
  }

  public static PuissanceFiscale fromFin(Integer fin) {
    return new PuissanceFiscale(null, fin);
  }

  public static PuissanceFiscale fromString(String rawValue) {
    if (rawValue == null || rawValue.isBlank()) {
      return null;
    }

    String value = rawValue.trim();

    try {
      if (value.startsWith(">=")) {
        return fromDebut(parse(value.substring(2)));
      }

      if (value.startsWith("<=")) {
        return fromFin(parse(value.substring(2)));
      }

      if (value.startsWith(">")) {
        return fromDebut(parse(value.substring(1)) + 1);
      }

      if (value.startsWith("<")) {
        return fromFin(parse(value.substring(1)) - 1);
      }

      if (value.contains(" à ")) {
        return parseInterval(value);
      }

      // Cas valeur unique
      int uniqueValue = parse(value);
      return of(uniqueValue, uniqueValue);

    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Format de puissance fiscale invalide : '" + rawValue + "'", e);
    }
  }

  private static PuissanceFiscale parseInterval(String value) {
    String[] parts = value.split(" à ");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Format invalide : attendu 'debut à fin'");
    }
    int debut = parse(parts[0]);
    int fin = parse(parts[1]);
    return of(debut, fin);
  }

  private static int parse(String s) {
    return Integer.parseInt(s.trim());
  }

  public boolean isInRange(Integer puissanceFiscale) {
    if (puissanceFiscale == null) {
      return false;
    }

    boolean afterStart = debut == null || puissanceFiscale >= debut;
    boolean beforeEnd = fin == null || puissanceFiscale <= fin;

    return afterStart && beforeEnd;
  }

  public boolean isExactMatch() {
    return Objects.equals(debut, fin);
  }

  @Override
  public @NotNull String toString() {
    if (isExactMatch()) {
      return String.valueOf(debut);
    }

    if (debut == null) {
      return "<= " + fin;
    }

    if (fin == null) {
      return ">= " + debut;
    }

    return debut + " à " + fin;
  }
}
