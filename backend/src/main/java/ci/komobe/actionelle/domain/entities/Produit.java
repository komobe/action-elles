package ci.komobe.actionelle.domain.entities;

import java.util.List;
import java.util.Optional;
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

    return categorieVehicules.stream().anyMatch(c -> c.getCode().equals(categorie));
  }

  public Optional<CategorieVehicule> getCategorieVehicule(String categorie) {
    return categorieVehicules.stream().filter(c -> c.getCode().equals(categorie)).findFirst();
  }
}
