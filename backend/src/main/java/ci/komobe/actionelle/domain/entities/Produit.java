package ci.komobe.actionelle.domain.entities;

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

  private String id;
  private String code;
  private String nom;
  private String description;
  private List<Garantie> garanties;
  private List<CategorieVehicule> categorieVehicules;

  public boolean contientCategorie(String categorie) {
    if (categorieVehicules == null) {
      return false;
    }

    return categorieVehicules.stream().anyMatch(c -> c.getCode().equals(categorie));
  }

  // Getters filtrés
  public List<Garantie> getGarantiesResponsabiliteCivile() {
    return garanties.stream()
        .filter(g -> "RC".equalsIgnoreCase(g.getCode()))
        .toList();
  }

  public List<Garantie> getAutresGaranties() {
    return garanties.stream()
        .filter(g -> !"RC".equalsIgnoreCase(g.getCode()))
        .toList();
  }

  public Optional<CategorieVehicule> recupererCategorieParCode(String categorieCode) {
    return categorieVehicules.stream().filter(c -> c.getCode().equals(categorieCode)).findFirst();
  }

  /**
   * Vérifie si un véhicule est éligible au produit.
   *
   * @param vehicule Le véhicule à vérifier
   * @return true si le véhicule est éligible, false sinon
   */
  public boolean estEligible(Vehicule vehicule) {
    if (categorieVehicules == null) {
      return false;
    }
    return categorieVehicules.stream().anyMatch(c -> c.getCode().equals(vehicule.getCategorie().getCode()));
  }
}
