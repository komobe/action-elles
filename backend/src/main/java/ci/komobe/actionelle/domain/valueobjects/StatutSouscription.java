package ci.komobe.actionelle.domain.valueobjects;

/**
 * Représente les différents états d'une souscription.
 */
public enum StatutSouscription {
  EN_ATTENTE_VALIDATION, REJETEE, VALIDEE;

  public boolean estEnAttenteValidation() {
    return this == EN_ATTENTE_VALIDATION;
  }

  public boolean estRejetee() {
    return this == REJETEE;
  }

  public boolean estValidee() {
    return this == VALIDEE;
  }
}