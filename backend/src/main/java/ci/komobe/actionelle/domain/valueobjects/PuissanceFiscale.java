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

  public static PuissanceFiscale fromString(String puissanceFiscale) {
    if (puissanceFiscale == null || puissanceFiscale.isBlank()) {
      return null; // Pas de valeur fournie
    }

    String puissanceFiscaleStrimmed = puissanceFiscale.trim();

    try {
      if (puissanceFiscaleStrimmed.startsWith("<=")) {
        // Cas borne supérieure uniquement : "<= valeur"
        String valeurBorneSupStr = puissanceFiscaleStrimmed.substring(3).trim();
        Integer borneSup = Integer.valueOf(valeurBorneSupStr);
        return PuissanceFiscale.fromFin(borneSup);
      } else if (puissanceFiscaleStrimmed.startsWith(">=")) {
        // Cas borne inférieure uniquement : ">= valeur"
        String valeurBorneInf = puissanceFiscaleStrimmed.substring(3).trim();
        Integer borneInf = Integer.valueOf(valeurBorneInf);
        return PuissanceFiscale.fromDebut(borneInf);
      } else if (puissanceFiscaleStrimmed.contains(" à ")) {
        // Cas intervalle : "debut à fin"
        String[] bornesStr = puissanceFiscaleStrimmed.split(" à ");
        if (bornesStr.length != 2) {
          throw new IllegalArgumentException("Format invalide : attendu 'debut à fin'");
        }
        Integer borneInf = Integer.valueOf(bornesStr[0].trim());
        Integer borneSup = Integer.valueOf(bornesStr[1].trim());
        return PuissanceFiscale.of(borneInf, borneSup);
      } else {
        // Cas valeur exacte unique
        Integer valeurExacte = Integer.valueOf(puissanceFiscaleStrimmed);
        return PuissanceFiscale.of(valeurExacte, valeurExacte);
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("La valeur de puissance fiscale contient un nombre invalide : '" + puissanceFiscaleStrimmed + "'", e);
    }
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
    return isExactMatch() ? "<= " + debut : debut + " à " + fin;
  }
}
