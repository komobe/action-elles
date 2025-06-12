package ci.komobe.actionelle.domain.utils;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.valueobjects.Prime;
import ci.komobe.actionelle.domain.valueobjects.PuissanceFiscale;
import ci.komobe.actionelle.domain.valueobjects.Role;
import ci.komobe.actionelle.domain.valueobjects.TypeBaseCalcul;
import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import ci.komobe.actionelle.domain.valueobjects.Valeur;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Générateur de données fictives pour les tests
 * 
 * @author Moro KONÉ 2025-05-28
 */
public class FakeGenerator {

  // Constantes pour la génération des véhicules
  private static final String[] COULEURS = { "Rouge", "Bleu", "Noir", "Blanc", "Gris", "Vert", "Jaune" };
  private static final String DEVISE = "XOF";
  private static final int MIN_PORTES = 2;
  private static final int MAX_PORTES_SUPPLEMENTAIRES = 3;
  private static final int MIN_PLACES = 2;
  private static final int MAX_PLACES_SUPPLEMENTAIRES = 5;
  private static final int MAX_AGE_VEHICULE_ANNEES = 10;
  private static final double VALEUR_NEUF_MIN = 8_000_000;
  private static final double VALEUR_NEUF_VARIATION = 1_000_000;
  private static final double TAUX_DEPRECIATION_MIN = 0.05;
  private static final double TAUX_DEPRECIATION_MAX = 0.10;

  // Constantes pour les catégories de véhicules
  private static final String CATEGORIE_201_CODE = "201";
  private static final String CATEGORIE_202_CODE = "202";
  private static final String CATEGORIE_203_CODE = "203";
  private static final String CATEGORIE_204_CODE = "204";

  // Constantes pour les produits
  private static final String CODE_RC = "RC";
  private static final String CODE_DOMMAGE = "DOMMAGE";
  private static final String CODE_VOL = "VOL";
  private static final String CODE_TIERCE_COLLISION = "TIERCE_COLLISION";
  private static final String CODE_INCENDIE = "INCENDIE";

  /**
   * Génère une liste de véhicules fictifs
   */
  public static List<Vehicule> generateVehicules(int count) {
    var categorieVehicules = generateCategorieVehicules();
    var random = new Random();
    var vehicules = new ArrayList<Vehicule>();

    for (int i = 0; i < count; i++) {
      vehicules.add(generateVehicule(i, random, categorieVehicules));
    }

    return vehicules;
  }

  /**
   * Génère un véhicule fictif
   */
  private static Vehicule generateVehicule(int index, Random random, List<CategorieVehicule> categories) {
    String immatriculation = String.format("CI-%05d-AB", index);
    String couleur = COULEURS[random.nextInt(COULEURS.length)];
    int portes = MIN_PORTES + random.nextInt(MAX_PORTES_SUPPLEMENTAIRES);
    int places = MIN_PLACES + random.nextInt(MAX_PLACES_SUPPLEMENTAIRES);
    int puissanceFiscale = 1 + random.nextInt(100);
    LocalDate dateMiseEnCirculation = LocalDate.now().minusYears(random.nextInt(MAX_AGE_VEHICULE_ANNEES));

    Valeur valeurNeuf = genererValeurNeuf(random);
    // Valeur valeurVenale = calculerValeurVenale(valeurNeuf,
    // dateMiseEnCirculation);

    return Vehicule.builder()
        .id(IdGenerator.generateId())
        .immatriculation(immatriculation)
        .couleur(couleur)
        .nombreDePortes(portes)
        .nombreDeSieges(places)
        .puissanceFiscale(puissanceFiscale)
        .dateMiseEnCirculation(dateMiseEnCirculation)
        .valeurNeuf(valeurNeuf)
        .categorie(categories.get(index % categories.size()))
        .build();
  }

  /**
   * Génère une valeur à neuf aléatoire
   */
  private static Valeur genererValeurNeuf(Random random) {
    double valeurBase = VALEUR_NEUF_MIN + random.nextDouble() * VALEUR_NEUF_VARIATION;
    return Valeur.of(valeurBase + random.nextDouble() * VALEUR_NEUF_VARIATION, DEVISE);
  }

  /**
   * Calcule la valeur vénale en fonction de la valeur à neuf et de l'âge
   */
  private static Valeur calculerValeurVenale(Valeur valeurNeuf, LocalDate dateMiseEnCirculation) {
    int age = Period.between(dateMiseEnCirculation, LocalDate.now()).getYears();
    double tauxDepreciation = TAUX_DEPRECIATION_MIN + (TAUX_DEPRECIATION_MAX - TAUX_DEPRECIATION_MIN) / 2;
    double valeurVenale = valeurNeuf.getMontant().doubleValue() * Math.pow(1 - tauxDepreciation, age);
    return Valeur.of(valeurVenale, DEVISE);
  }

  /**
   * Génère la liste des catégories de véhicules
   */
  public static List<CategorieVehicule> generateCategorieVehicules() {
    return List.of(
        creerCategorieVehicule(CATEGORIE_201_CODE, "Promenade et Affaire", "Usage personnel"),
        creerCategorieVehicule(CATEGORIE_202_CODE, "Véhicules Motorisés à 2 ou 3 roues", "Motocycle, tricycles"),
        creerCategorieVehicule(CATEGORIE_203_CODE, "Transport public de voyage", "Véhicule transport de personnes"),
        creerCategorieVehicule(CATEGORIE_204_CODE, "Véhicule de transport avec taximètres", "Taxis"));
  }

  /**
   * Crée une catégorie de véhicule
   */
  private static CategorieVehicule creerCategorieVehicule(String code, String libelle, String description) {
    return CategorieVehicule.builder()
        .code(code)
        .libelle(libelle)
        .description(description)
        .build();
  }

  /**
   * Génère la liste des produits avec leurs garanties
   */
  public static List<Produit> generateProduits() {
    var categorieVehicules = Map.of(
        CATEGORIE_201_CODE, creerCategorieVehicule(CATEGORIE_201_CODE, "Promenade et Affaire", "Usage personnel"),
        CATEGORIE_202_CODE,
        creerCategorieVehicule(CATEGORIE_202_CODE, "Véhicules Motorisés à 2 ou 3 roues", "Motocycle, tricycles"));

    var garanties = new ArrayList<Garantie>();
    garanties.addAll(genererGarantiesRC());
    garanties.addAll(genererGarantiesOptionnelles());

    return creerProduits(categorieVehicules, garanties);
  }

  /**
   * Génère les garanties RC avec leurs primes par puissance fiscale
   */
  private static List<Garantie> genererGarantiesRC() {
    Map<PuissanceFiscale, BigDecimal> puissanceFiscaleToPrime = new HashMap<>();
    puissanceFiscaleToPrime.put(PuissanceFiscale.fromDebut(2), BigDecimal.valueOf(37_601));
    puissanceFiscaleToPrime.put(PuissanceFiscale.of(3, 6), BigDecimal.valueOf(45_181));
    puissanceFiscaleToPrime.put(PuissanceFiscale.of(7, 10), BigDecimal.valueOf(51_078));
    puissanceFiscaleToPrime.put(PuissanceFiscale.of(11, 14), BigDecimal.valueOf(65_677));
    puissanceFiscaleToPrime.put(PuissanceFiscale.of(15, 23), BigDecimal.valueOf(86_456));
    puissanceFiscaleToPrime.put(PuissanceFiscale.fromFin(24), BigDecimal.valueOf(104_143));

    var garanties = new ArrayList<Garantie>();
    for (var entry : puissanceFiscaleToPrime.entrySet()) {
      garanties.add(Garantie.builder()
          .id(IdGenerator.generateId())
          .libelle("Responsabilité Civile")
          .description("Responsabilité Civile")
          .code("RC")
          .puissanceFiscale(entry.getKey())
          .baseDeCalcul(TypeBaseCalcul.PUISSANCE_FISCALE)
          .prime(new Prime(TypeMontantPrime.MONTANT, entry.getValue()))
          .maxAge(-1) // Pas de limite d'âge
          .plafonne(false)
          .build());
    }
    return garanties;
  }

  /**
   * Génère les garanties optionnelles
   */
  private static List<Garantie> genererGarantiesOptionnelles() {
    return List.of(
        creerGarantieDommage(),
        creerGarantieTierceCollision(),
        creerGarantieTiercePlafonnee(),
        creerGarantieVol(),
        creerGarantieIncendie());
  }

  private static Garantie creerGarantieDommage() {
    return Garantie.builder()
        .id(IdGenerator.generateId())
        .libelle("Dommages")
        .description("Dommages")
        .code("DOMMAGE")
        .baseDeCalcul(TypeBaseCalcul.VALEUR_A_NEUF)
        .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(2.6)))
        .maxAge(5)
        .plafonne(false)
        .build();
  }

  private static Garantie creerGarantieTierceCollision() {
    return Garantie.builder()
        .id(IdGenerator.generateId())
        .libelle("Tierce Collision")
        .description("Tierce Collision")
        .code("TIERCE_COLLISION")
        .baseDeCalcul(TypeBaseCalcul.VALEUR_A_NEUF)
        .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(1.65)))
        .maxAge(8)
        .plafonne(false)
        .build();
  }

  private static Garantie creerGarantieTiercePlafonnee() {
    return Garantie.builder()
        .id(IdGenerator.generateId())
        .libelle("Tierce Plafonnée")
        .description("Tierce Plafonnée")
        .code("TIERCE_PLAFONNEE")
        .baseDeCalcul(TypeBaseCalcul.VALEUR_ASSUREE)
        .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(4.20)))
        .primeMinimum(new Prime(TypeMontantPrime.MONTANT, BigDecimal.valueOf(100_000)))
        .maxAge(10)
        .plafonne(true)
        .build();
  }

  private static Garantie creerGarantieVol() {
    return Garantie.builder()
        .id(IdGenerator.generateId())
        .libelle("Vol")
        .description("Vol")
        .code("VOL")
        .baseDeCalcul(TypeBaseCalcul.VALEUR_VENALE)
        .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(0.14)))
        .maxAge(-1)
        .plafonne(false)
        .build();
  }

  private static Garantie creerGarantieIncendie() {
    return Garantie.builder()
        .id(IdGenerator.generateId())
        .libelle("Incendie")
        .description("Incendie")
        .code("INCENDIE")
        .baseDeCalcul(TypeBaseCalcul.VALEUR_VENALE)
        .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(0.15)))
        .maxAge(-1)
        .plafonne(false)
        .build();
  }

  /**
   * Crée les produits avec leurs garanties
   */
  private static List<Produit> creerProduits(Map<String, CategorieVehicule> categories, List<Garantie> garanties) {
    var produits = new ArrayList<Produit>();
    var garantiesParCode = regrouperGarantiesParCode(garanties);

    // Produit "Papillon" : RC, DOMMAGE, VOL
    produits.add(Produit.builder()
        .id(IdGenerator.generateId())
        .nom("Papillon")
        .description("RC, DOMMAGE, VOL")
        .categoriesVehicules(List.of(categories.get(CATEGORIE_201_CODE)))
        .garanties(extraireGaranties(garantiesParCode, CODE_RC, CODE_DOMMAGE, CODE_VOL))
        .build());

    // Produit "Douby" : RC, DOMMAGE, TIERCE COLLISION
    produits.add(Produit.builder()
        .id(IdGenerator.generateId())
        .nom("Douby")
        .description("RC, DOMMAGE, TIERCE COLLISION")
        .categoriesVehicules(List.of(categories.get(CATEGORIE_202_CODE)))
        .garanties(extraireGaranties(garantiesParCode, CODE_RC, CODE_DOMMAGE, CODE_TIERCE_COLLISION))
        .build());

    // Produit "Douyou" : RC, DOMMAGE, COLLISION, INCENDIE
    produits.add(Produit.builder()
        .id(IdGenerator.generateId())
        .nom("Douyou")
        .description("RC, DOMMAGE, COLLISION, INCENDIE")
        .categoriesVehicules(List.of(categories.get(CATEGORIE_201_CODE), categories.get(CATEGORIE_202_CODE)))
        .garanties(extraireGaranties(garantiesParCode, CODE_RC, CODE_DOMMAGE, CODE_TIERCE_COLLISION, CODE_INCENDIE))
        .build());

    // Produit "Toutourisquou" : Toutes garanties
    produits.add(Produit.builder()
        .id(IdGenerator.generateId())
        .nom("Toutourisquou")
        .description("Toutes garanties")
        .categoriesVehicules(List.of(categories.get(CATEGORIE_201_CODE)))
        .garanties(garanties)
        .build());

    return produits;
  }

  /**
   * Regroupe les garanties par code pour faciliter leur extraction
   */
  private static Map<String, List<Garantie>> regrouperGarantiesParCode(List<Garantie> garanties) {
    var garantiesParCode = new HashMap<String, List<Garantie>>();
    for (Garantie garantie : garanties) {
      garantiesParCode.computeIfAbsent(garantie.getCode(), k -> new ArrayList<>()).add(garantie);
    }
    return garantiesParCode;
  }

  /**
   * Extrait les garanties correspondant aux codes spécifiés
   */
  private static List<Garantie> extraireGaranties(Map<String, List<Garantie>> garantiesParCode, String... codes) {
    var garantiesSelectionnees = new ArrayList<Garantie>();
    for (String code : codes) {
      if (garantiesParCode.containsKey(code)) {
        garantiesSelectionnees.addAll(garantiesParCode.get(code));
      }
    }
    return garantiesSelectionnees;
  }

  /**
   * Génère les utilisateurs de test
   */
  public static Map<String, Utilisateur> generateUtilisateurs() {
    var utilisateurs = new HashMap<String, Utilisateur>();

    // Utilisateur admin
    var admin = new Utilisateur();
    admin.setId(IdGenerator.generateId());
    admin.setUsername("admin");
    admin.setPassword("admin123");
    admin.setRole(Role.ADMIN);
    utilisateurs.put(admin.getUsername(), admin);

    // Utilisateur amazone
    var amazone = new Utilisateur();
    amazone.setId(IdGenerator.generateId());
    amazone.setUsername("amazone");
    amazone.setPassword("amazone123");
    amazone.setRole(Role.AMAZONE);
    utilisateurs.put(amazone.getUsername(), amazone);

    // Autres utilisateurs de test
    creerUtilisateur(utilisateurs, "gabriella", "monsupermotdepasse", Role.AMAZONE);
    creerUtilisateur(utilisateurs, "stephanie", "autrepassword", Role.AMAZONE);
    creerUtilisateur(utilisateurs, "robine", "monsupermotdepasse", Role.AMAZONE);

    return utilisateurs;
  }

  /**
   * Crée un utilisateur et l'ajoute à la map
   */
  private static void creerUtilisateur(Map<String, Utilisateur> utilisateurs, String username, String password,
      Role role) {
    var utilisateur = new Utilisateur();
    utilisateur.setId(IdGenerator.generateId());
    utilisateur.setUsername(username);
    utilisateur.setPassword(password);
    utilisateur.setRole(role);
    utilisateurs.put(username, utilisateur);
  }
}
