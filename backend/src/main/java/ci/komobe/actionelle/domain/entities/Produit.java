package ci.komobe.actionelle.domain.entities;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité Produit
 *
 * @author Moro KONÉ 2025-06-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Produit {

  public static final String GARANTIE_RC_CODE = "RC";

  private String id;
  private String code;
  private String nom;
  private String description;
  private List<Garantie> garanties;
  private List<CategorieVehicule> categoriesVehicules;

  public boolean contientCategorie(String categorie) {
    return getCategoriesVehicules().stream()
        .anyMatch(c -> c.getCode().equals(categorie));
  }

  /**
   * Vérifie si ce produit peut assurer le véhicule donné.
   *
   * @return true si le produit accepte ce véhicule
   */
  public boolean peutAssurer(Vehicule vehicule) {
    if (vehicule == null || vehicule.getCategorie() == null) {
      return false;
    }
    return contientCategorie(vehicule.getCategorie().getCode());
  }

  /**
   * Vérifie si ce produit accepte le véhicule donné.
   */
  public boolean accepte(Vehicule vehicule) {
    return peutAssurer(vehicule);
  }

  /**
   * Vérifie si ce produit ne peut pas assurer le véhicule donné.
   *
   * @return true si le produit ne peut pas assurer ce véhicule
   */
  public boolean nePeutPasAssurer(Vehicule vehicule) {
    return !accepte(vehicule);
  }


  public List<Garantie> getGaranties() {
    return garanties != null ? garanties : Collections.emptyList();
  }

  public List<CategorieVehicule> getCategoriesVehicules() {
    return categoriesVehicules != null ? categoriesVehicules : Collections.emptyList();
  }


  public List<Garantie> obtenirGarantiesRC() {
    return getGaranties().stream()
        .filter(garantie -> GARANTIE_RC_CODE.equalsIgnoreCase(garantie.getCode()))
        .toList();
  }

  public List<Garantie> obtenirAutresGaranties() {
    return getGaranties().stream()
        .filter(garantie -> !GARANTIE_RC_CODE.equalsIgnoreCase(garantie.getCode()))
        .toList();
  }

  public Optional<CategorieVehicule> chercherCategorieParCode(String categorieCode) {
    return getCategoriesVehicules().stream()
        .filter(c -> c.getCode().equals(categorieCode))
        .findFirst();
  }

  /**
   * Récupère la garantie RC eligible pour l'âge et puissance fiscale donnés
   */
  public Garantie obtenirGarantieRCEligible(double age, int puissanceFiscale) {
    return obtenirGarantiesRC().stream()
        .filter(garantie -> garantie.estEligible(age))
        .filter(garantie -> garantie.getPuissanceFiscale().isInRange(puissanceFiscale))
        .findFirst().orElseThrow(() -> new IllegalStateException(
            String.format(
                "Aucune garantie RC trouvée pour âge %.2f et puissance fiscale %d",
                age, puissanceFiscale
            ))
        );
  }

  /**
   * Récupère toutes les garanties applicables (hors RC) pour un âge donné
   */
  public List<Garantie> obtenirGarantiesEligibles(double age) {
    return obtenirAutresGaranties().stream()
        .filter(garantie -> garantie.estEligible(age))
        .toList();
  }

  /**
   * Vérifie si le produit a au moins une garantie RC configurée
   */
  public boolean possedeGarantieRC() {
    return !obtenirGarantiesRC().isEmpty();
  }


  /**
   * Valide que le produit est correctement configuré
   */
  public void validerConfiguration() {
    if (code == null || code.trim().isEmpty()) {
      throw new IllegalStateException("Le code produit est obligatoire");
    }

    if (nom == null || nom.trim().isEmpty()) {
      throw new IllegalStateException("Le nom du produit est obligatoire");
    }

    if (!possedeGarantieRC()) {
      throw new IllegalStateException("Le produit doit avoir au moins une garantie RC");
    }

    if (getCategoriesVehicules().isEmpty()) {
      throw new IllegalStateException(
          "Le produit doit être associé à au moins une catégorie de véhicule");
    }
  }
}
