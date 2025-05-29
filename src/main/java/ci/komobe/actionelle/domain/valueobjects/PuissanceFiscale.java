package ci.komobe.actionelle.domain.valueobjects;

import jakarta.validation.constraints.NotNull;

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

  public boolean isInRange(Integer puissanceFiscale) {
    if (puissanceFiscale == null) {
      return false;
    }

    if (debut == null) {
      return puissanceFiscale <= fin;
    }
    if (fin == null) {
      return puissanceFiscale >= debut;
    }
    return puissanceFiscale >= debut && puissanceFiscale <= fin;
  }

  public boolean isExactMatch() {
    return debut != null && debut.equals(fin);
  }

  @Override
  public @NotNull String toString() {
    if (debut == null) {
      return "<= " + fin;
    }
    if (fin == null) {
      return ">= " + debut;
    }
    return debut.equals(fin) ? String.valueOf(debut) : debut + " à " + fin;
  }
}
