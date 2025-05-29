package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.valueobjects.CategorieVehicule;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-28
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"id"})
public class Produit {

  private String id;
  private String nom;
  private String description;
  private List<Garantie> garantiesResponsabiliteCivile;
  private List<Garantie> autresGaranties;
  private List<CategorieVehicule> categorieVehicules;

  public boolean contientCategorie(String categorie) {
    if (categorieVehicules == null) {
      return false;
    }

    return categorieVehicules.stream().anyMatch(c -> c.code().equals(categorie));
  }
}
