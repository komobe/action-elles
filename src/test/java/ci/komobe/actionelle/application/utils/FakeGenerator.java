package ci.komobe.actionelle.application.utils;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import ci.komobe.actionelle.domain.valueobjects.Prime;
import ci.komobe.actionelle.domain.valueobjects.PuissanceFiscale;
import ci.komobe.actionelle.domain.valueobjects.TypeBaseCalcul;
import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class FakeGenerator {

  public static List<Vehicule> generateVehicules(int count) {
    String[] couleurs = { "Rouge", "Bleu", "Noir", "Blanc", "Gris", "Vert", "Jaune" };
    var random = new Random();
    var vehicules = new ArrayList<Vehicule>();

    for (int i = 1; i <= count; i++) {
      String immatriculation = String.format("CI-%05d-AB", i);
      String couleur = couleurs[random.nextInt(couleurs.length)];
      int portes = 2 + random.nextInt(3); // 2 à 4 portes
      int places = 2 + random.nextInt(5); // 2 à 6 places

      Vehicule vehicule = new Vehicule();
      vehicule.setNumeroImmatriculation(immatriculation);
      vehicule.setCouleur(couleur);
      vehicule.setNombreDePortes(portes);
      vehicule.setNombreDeSieges(places);
      vehicule.setDateMiseEnCirculation(LocalDate.now());
      var categorieVehicule = CategorieVehicule.builder()
          .id(IdGenerator.generateId())
          .code("201")
          .libelle("Voiture")
          .description("Voiture de classe")
          .build();
      vehicule.setCategorie(categorieVehicule);

      vehicules.add(vehicule);
    }

    return vehicules;
  }

  public static List<CategorieVehicule> generateCategorieVehicules() {
    var categorieVehicules = new ArrayList<CategorieVehicule>();
    categorieVehicules.add(CategorieVehicule.builder()
        .code("201")
        .libelle("Promenade et Affaire")
        .description("Usage personnel")
        .build());
    categorieVehicules.add(
        CategorieVehicule.builder()
            .code("202")
            .libelle("Véhicules Motorisés à 2 ou 3 roues")
            .description("Motocycle, tricycles")
            .build());
    categorieVehicules.add(CategorieVehicule.builder()
        .code("203")
        .libelle("Transport public de voyage")
        .description("Véhicule transport de personnes")
        .build());
    categorieVehicules.add(
        CategorieVehicule.builder()
            .code("204")
            .libelle("Véhicule de transport avec taximètres")
            .description("Taxis")
            .build());
    return categorieVehicules;
  }

  public static List<Produit> generateProduits() {

    // Définitions préliminaires
    var categorie201 = CategorieVehicule.builder()
        .id(IdGenerator.generateId())
        .code("201")
        .libelle("Promenade et Affaire")
        .description("Usage personnel")
        .build();
    var categorie202 = CategorieVehicule.builder()
        .id(IdGenerator.generateId())
        .code("202")
        .libelle("Véhicules Motorisés à 2 ou 3 roues")
        .description("Motocycle, tricycles")
        .build();

    // Garanties
    Map<PuissanceFiscale, BigDecimal> puissanceFiscaleToPrime = new HashMap<>();
    puissanceFiscaleToPrime.put(PuissanceFiscale.fromDebut(2), BigDecimal.valueOf(37_601));
    puissanceFiscaleToPrime.put(PuissanceFiscale.of(3, 6), BigDecimal.valueOf(45_181));
    puissanceFiscaleToPrime.put(PuissanceFiscale.of(7, 10), BigDecimal.valueOf(51_078));
    puissanceFiscaleToPrime.put(PuissanceFiscale.of(11, 14), BigDecimal.valueOf(65_677));
    puissanceFiscaleToPrime.put(PuissanceFiscale.of(15, 23), BigDecimal.valueOf(86_456));
    puissanceFiscaleToPrime.put(PuissanceFiscale.fromFin(24), BigDecimal.valueOf(104_143));

    var garantiesRC = new ArrayList<Garantie>();
    for (var entry : puissanceFiscaleToPrime.entrySet()) {
      Garantie garantie = Garantie.builder()
          .id(IdGenerator.generateId())
          .libelle("Responsabilité Civile")
          .description("Responsabilité Civile")
          .code("RC")
          .puissanceFiscale(entry.getKey())
          .baseDeCalcul(TypeBaseCalcul.PUISSANCE_FISCALE)
          .prime(new Prime(TypeMontantPrime.MONTANT, entry.getValue()))
          .maxAge(-1) // Pas de limite d'âge
          .plafonne(false)
          .build();

      garantiesRC.add(garantie);
    }

    Garantie garantieDommage = Garantie.builder()
        .id(IdGenerator.generateId())
        .libelle("Dommages")
        .description("Dommages")
        .code("DOMMAGE")
        .baseDeCalcul(TypeBaseCalcul.VALEUR_A_NEUF)
        .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(2.6)))
        .maxAge(5)
        .plafonne(false)
        .build();

    Garantie garantieTierceCollision = Garantie.builder()
        .id(IdGenerator.generateId())
        .libelle("Tierce Collision")
        .description("Tierce Collision")
        .code("TIERCE_COLLISION")
        .baseDeCalcul(TypeBaseCalcul.VALEUR_A_NEUF)
        .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(1.65)))
        .maxAge(8)
        .plafonne(false)
        .build();

    Garantie garantieTiercePlafonnee = Garantie.builder()
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

    Garantie garantieVol = Garantie.builder()
        .id(IdGenerator.generateId())
        .libelle("Vol")
        .description("Vol")
        .code("VOL")
        .baseDeCalcul(TypeBaseCalcul.VALEUR_VENALE)
        .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(0.14)))
        .maxAge(-1)
        .plafonne(false)
        .build();

    Garantie garantieIncendie = Garantie.builder()
        .id(IdGenerator.generateId())
        .libelle("Incendie")
        .description("Incendie")
        .code("INCENDIE")
        .baseDeCalcul(TypeBaseCalcul.VALEUR_VENALE)
        .prime(new Prime(TypeMontantPrime.POURCENTAGE, BigDecimal.valueOf(0.15)))
        .maxAge(-1)
        .plafonne(false)
        .build();

    var produits = new ArrayList<Produit>();

    // RC, DOMMAGE, VOL
    Produit produit = Produit.builder()
        .nom("Papillon")
        .description("RC, DOMMAGE, VOL")
        .categorieVehicules(List.of(categorie201))
        .garantiesResponsabiliteCivile(garantiesRC)
        .autresGaranties(List.of(garantieDommage, garantieVol))
        .build();

    produits.add(produit);

    // RC, DOMMAGE, TIERCE COLLISION
    produit = Produit.builder()
        .nom("Douby")
        .description("RC, DOMMAGE, TIERCE COLLISION")
        .categorieVehicules(
            List.of(categorie202))
        .garantiesResponsabiliteCivile(garantiesRC)
        .autresGaranties(List.of(garantieDommage, garantieTierceCollision))
        .build();

    produits.add(produit);

    // RC, DOMMAGE, COLLISION, INCENDIE
    produit = Produit.builder()
        .nom("Douyou")
        .description("RC, DOMMAGE, COLLISION, INCENDIE")
        .categorieVehicules(List.of(categorie201, categorie202))
        .garantiesResponsabiliteCivile(garantiesRC)
        .autresGaranties(List.of(garantieDommage, garantieTierceCollision, garantieIncendie))
        .build();

    produits.add(produit);

    // Toutes garanties
    produit = Produit.builder()
        .nom("Toutourisquou")
        .description("Toutes garanties")
        .categorieVehicules(List.of(categorie201))
        .garantiesResponsabiliteCivile(garantiesRC)
        .autresGaranties(List.of(
            garantieDommage, garantieTierceCollision,
            garantieTiercePlafonnee, garantieVol,
            garantieIncendie))
        .build();

    produits.add(produit);

    return produits;
  }

  public static Map<String, Utilisateur> generateUtilisateurs() {

    var utilisateurs = new HashMap<String, Utilisateur>();

    var utilisateurGabriella = new Utilisateur();
    utilisateurGabriella.inscrire("gabriella", "monsupermotdepasse");
    utilisateurs.put("gabriella", utilisateurGabriella);

    var utilisateurStephanie = new Utilisateur();
    utilisateurStephanie.inscrire("stephanie", "autrepassword");
    utilisateurs.put("stephanie", utilisateurStephanie);

    var utilisateurRobine = new Utilisateur();
    utilisateurRobine.inscrire("robine", "monsupermotdepasse");
    utilisateurs.put("robine", utilisateurRobine);

    return utilisateurs;
  }

}
