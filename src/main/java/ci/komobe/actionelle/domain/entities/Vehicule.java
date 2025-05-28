package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.valueobjects.CategorieVehicule;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-28
 */

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
public class Vehicule {

  private String id;
  private LocalDate dateMiseEnCirculation;
  private String numeroImmatriculation;
  private String couleur;
  private int nombreDeSieges;
  private int nombreDePortes;
  private CategorieVehicule categorie;

  public Vehicule(String id) {
    this.id = id;
  }
}
