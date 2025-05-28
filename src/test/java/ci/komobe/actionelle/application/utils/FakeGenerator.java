package ci.komobe.actionelle.application.utils;

import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.valueobjects.CategorieVehicule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
      vehicule.setCategorie(new CategorieVehicule("201", "Voiture", "Voiture de classe"));

      vehicules.add(vehicule);
    }

    return vehicules;
  }

  public static List<CategorieVehicule> generateCategorieVehicules() {
    var categorieVehicules = new ArrayList<CategorieVehicule>();
    categorieVehicules.add(new CategorieVehicule("201", "Promenade et Affaire", "Usage personnel"));
    categorieVehicules.add(new CategorieVehicule("202", "Véhicules Motorisés à 2 ou 3 roues", "Motocycle, tricycles"));
    categorieVehicules
        .add(new CategorieVehicule("203", "Transport public de voyage", "Véhicule transport de personnes"));
    categorieVehicules.add(new CategorieVehicule("204", "Véhicule de transport avec taximètres", "Taxis"));
    return categorieVehicules;
  }

}
