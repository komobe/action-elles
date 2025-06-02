package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.valueobjects.Valeur;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité Véhicule
 *
 * @author Moro KONÉ 2025-06-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {

  private String id;
  private String immatriculation;
  private String couleur;
  private Integer nombreDeSieges;
  private Integer nombreDePortes;
  private LocalDate dateMiseEnCirculation;
  private Valeur valeurNeuf;
  private Integer puissanceFiscale;
  private CategorieVehicule categorie;

  public void mettreAjourDepuis(Vehicule vehicule) {
    if (vehicule == null) {
      return;
    }

    verifierCaracteristiquesTechniques(vehicule);

    // Mettre à jour les champs sauf id
    this.dateMiseEnCirculation = vehicule.getDateMiseEnCirculation();
    this.immatriculation = vehicule.getImmatriculation();
    this.couleur = vehicule.getCouleur();
    this.nombreDeSieges = vehicule.getNombreDeSieges();
    this.nombreDePortes = vehicule.getNombreDePortes();
    this.puissanceFiscale = vehicule.getPuissanceFiscale();
    this.valeurNeuf = vehicule.getValeurNeuf();
    this.categorie = vehicule.getCategorie();
  }

  private void verifierCaracteristiquesTechniques(Vehicule vehicule) {
    var erreurMap = new HashMap<String, String>();
    if (this.categorie != null && !Objects.equals(this.categorie, vehicule.getCategorie())) {
      erreurMap.put("categorie", "La catégorie du véhicule ne peut pas être modifiée");
    }
    /*
     * if (!Objects.equals(this.couleur, vehicule.getCouleur())) {
     * erreurMap.add("La couleur du véhicule ne peut pas être modifiée");
     * }
     */
    if (this.nombreDeSieges != 0 && !Objects.equals(this.nombreDeSieges,
        vehicule.getNombreDeSieges())) {
      erreurMap.put("nombreDeSieges", "Le nombre de sièges du véhicule ne peut pas être modifié");
    }
    if (this.nombreDePortes != 0 && !Objects.equals(this.nombreDePortes,
        vehicule.getNombreDePortes())) {
      erreurMap.put("nombreDePortes", "Le nombre de portes du véhicule ne peut pas être modifié");
    }
    if (this.puissanceFiscale != 0 && !Objects.equals(this.puissanceFiscale,
        vehicule.getPuissanceFiscale())) {
      erreurMap.put("puissanceFiscale",
          "La puissance fiscale du véhicule ne peut pas être modifiée");
    }
    if (this.valeurNeuf != null && !Objects.equals(this.valeurNeuf, vehicule.getValeurNeuf())) {
      erreurMap.put("valeurNeuf", "La valeur neuf du véhicule ne peut pas être modifiée");
    }
    if (!erreurMap.isEmpty()) {
      throw new IllegalArgumentException(
          "Informations du véhicule incorrectes : " + String.join(", ", erreurMap.values())
              + " car il existe un vehicule avec l'immatriculation " + this.immatriculation
              + " et d'autres informations différentes.");
    }
  }
}
