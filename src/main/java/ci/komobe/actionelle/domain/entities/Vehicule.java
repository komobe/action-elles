package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.valueobjects.CategorieVehicule;
import java.time.LocalDate;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-28
 */

@Getter
@Setter
@EqualsAndHashCode(of = { "id" })
public class Vehicule {

  private UUID id;
  private LocalDate dateMiseEnCirculation;
  private String numeroImmatriculation;
  private String couleur;
  private int nombreDeSieges;
  private int nombreDePortes;
  private CategorieVehicule categorie;
}
