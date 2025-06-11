package ci.komobe.actionelle.application.commons.services.prime;

import ci.komobe.actionelle.application.features.devis.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Moro KONÉ 2025-05-29
 */
public record PrimeCalculator(Map<TypeMontantPrime, PrimeCalculationStrategy> strategies) {

  public static final double NOMBRE_JOURS_ANNEE_NORMALE = 365.0;
  public static final double NOMBRE_JOURS_ANNEE_BISSEXTILE = 366.0;

  public static PrimeCalculatorBuilder builder() {
    return new PrimeCalculatorBuilder();
  }

  /**
   * Calcule la prime totale pour un produit et une commande donnés.
   *
   * @param produit Le produit d'assurance
   * @param command La commande de simulation contenant les paramètres
   * @return Le montant total de la prime
   * @throws IllegalArgumentException Si les paramètres sont invalides
   * @throws IllegalStateException    Si aucune garantie RC n'est trouvée
   */
  public BigDecimal calculPrime(Produit produit, SimulerPrimeCommand command) {
    validateInputs(produit, command);

    double age = calculerAge(command);

    // Garantie RC (obligatoire)
    BigDecimal primeRc = calculerPrimeGarantieRC(produit, age, command);

    // Autres garanties (optionnelles)
    BigDecimal autresPrimes = calculerPrimeAutresGaranties(produit, age, command);

    return primeRc.add(autresPrimes);
  }

  private void validateInputs(Produit produit, SimulerPrimeCommand command) {
    if (produit == null) {
      throw new IllegalArgumentException("Le produit ne peut pas être null");
    }

    if (command == null) {
      throw new IllegalArgumentException("La commande ne peut pas être null");
    }

    if (command.getDateDeMiseEnCirculation() == null) {
      throw new IllegalArgumentException("La date de mise en circulation est obligatoire");
    }

    if (command.getValeurVenale() == null
        || command.getValeurVenale().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("La valeur vénale doit être positive");
    }

    if (command.getPuissanceFiscale() <= 0) {
      throw new IllegalArgumentException("La puissance fiscale doit être positive");
    }

    produit.validerConfiguration();
  }

  private BigDecimal calculerPrimeGarantieRC(Produit produit, double age,
      SimulerPrimeCommand command) {
    Garantie garantieRc = produit.obtenirGarantieRCEligible(age, command.getPuissanceFiscale());
    return calculer(garantieRc, command);
  }

  private BigDecimal calculerPrimeAutresGaranties(
      Produit produit, double age,
      SimulerPrimeCommand command
  ) {
    return produit.obtenirGarantiesEligibles(age).stream()
        .map(garantie -> {
          BigDecimal montant = calculer(garantie, command);
          return garantie.appliquerPlafonnement(montant, command.getValeurVenale());
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private BigDecimal calculer(Garantie garantie, SimulerPrimeCommand command) {
    PrimeCalculationStrategy strategy = strategies.get(garantie.getPrime().type());

    if (strategy == null) {
      throw new IllegalStateException(
          "Aucune stratégie trouvée pour le type " + garantie.getPrime().type());
    }

    return strategy.isApplicable(garantie) ? strategy.calculer(garantie, command) : BigDecimal.ZERO;
  }

  /**
   * Calcule l'âge en années en tenant compte précisément des années bissextiles.
   *
   * @param command La commande contenant la date de mise en circulation
   * @return L'âge en années (décimal)
   * @throws IllegalArgumentException Si la date est future
   */
  private double calculerAge(SimulerPrimeCommand command) {
    LocalDate dateMiseEnCirculation = command.getDateDeMiseEnCirculation();
    LocalDate maintenant = LocalDate.now();

    if (dateMiseEnCirculation.isAfter(maintenant)) {
      throw new IllegalArgumentException("La date de mise en circulation ne peut pas être future");
    }

    long totalJours = ChronoUnit.DAYS.between(dateMiseEnCirculation, maintenant);

    if (totalJours == 0) {
      return 0.0;
    }

    double joursMoyensParAn = calculerNombreJoursMoyensParAn(dateMiseEnCirculation, maintenant);

    return totalJours / joursMoyensParAn;
  }

  /**
   * Calcule le nombre moyen de jours par an pour une période donnée en tenant compte des années
   * bissextiles réelles.
   *
   * @param dateDebut Date de début de la période
   * @param dateFin   Date de fin de la période
   * @return Nombre moyen de jours par an
   */
  private double calculerNombreJoursMoyensParAn(LocalDate dateDebut, LocalDate dateFin) {
    long joursBissextiles = compterNombreJoursBissextils(dateDebut, dateFin);

    // Nombre d'années complètes dans la période
    long nombreAnneesCompletes = ChronoUnit.YEARS.between(dateDebut, dateFin);

    if (nombreAnneesCompletes == 0) {
      // Période de moins d'un an : on regarde si elle contient un 29 février
      return joursBissextiles > 0 ? NOMBRE_JOURS_ANNEE_BISSEXTILE : NOMBRE_JOURS_ANNEE_NORMALE;
    }

    // Pour les longues périodes : moyenne réelle
    return NOMBRE_JOURS_ANNEE_NORMALE + ((double) joursBissextiles / nombreAnneesCompletes);
  }

  /**
   * Compte le nombre de jours bissextils (29 février) entre deux dates (incluses).
   *
   * @param dateDebut Date de début (incluse)
   * @param dateFin   Date de fin (incluse)
   * @return Nombre de 29 février dans la période
   */
  private long compterNombreJoursBissextils(LocalDate dateDebut, LocalDate dateFin) {
    return dateDebut.getYear() <= dateFin.getYear() ?
        dateDebut.datesUntil(dateFin.plusDays(1))
            .filter(date -> date.getMonthValue() == 2 && date.getDayOfMonth() == 29)
            .count() : 0;
  }


  /**
   * Builder pour construire des instances de PrimeCalculator de manière fluide.
   */
  public static class PrimeCalculatorBuilder {

    private final Map<TypeMontantPrime, PrimeCalculationStrategy> strategies
        = new EnumMap<>(TypeMontantPrime.class);

    /**
     * Ajoute une stratégie de calcul pour un type de montant donné.
     *
     * @param type     Le type de montant
     * @param strategy La stratégie de calcul
     * @return Ce builder pour chaînage
     * @throws IllegalArgumentException Si un paramètre est null
     */
    public PrimeCalculatorBuilder addStrategy(
        TypeMontantPrime type,
        PrimeCalculationStrategy strategy
    ) {
      if (type == null) {
        throw new IllegalArgumentException("Le type de stratégie ne peut pas être null");
      }
      if (strategy == null) {
        throw new IllegalArgumentException("La stratégie ne peut pas être null");
      }

      strategies.put(type, strategy);

      return this;
    }

    /**
     * Construit l'instance finale de PrimeCalculator.
     *
     * @return Une nouvelle instance immutable de PrimeCalculator
     * @throws IllegalStateException Si la configuration est invalide
     */
    public PrimeCalculator build() {
      validateConfiguration();
      return new PrimeCalculator(Map.copyOf(strategies));
    }

    /**
     * Valide la configuration du builder avant construction.
     *
     * @throws IllegalStateException Si la configuration est invalide
     */
    private void validateConfiguration() {
      if (strategies.isEmpty()) {
        throw new IllegalStateException("Au moins une stratégie doit être configurée");
      }

      // Vérification des stratégies essentielles
      if (!strategies.containsKey(TypeMontantPrime.POURCENTAGE)) {
        throw new IllegalStateException("La stratégie POURCENTAGE est obligatoire");
      }

      if (!strategies.containsKey(TypeMontantPrime.MONTANT)) {
        throw new IllegalStateException("La stratégie MONTANT est obligatoire");
      }
    }
  }
}