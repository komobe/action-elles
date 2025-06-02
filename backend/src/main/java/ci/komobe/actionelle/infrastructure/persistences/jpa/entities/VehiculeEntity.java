package ci.komobe.actionelle.infrastructure.persistences.jpa.entities;

import ci.komobe.actionelle.domain.valueobjects.Valeur;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "vehicules")
public class VehiculeEntity {

  @Id
  private String id;

  private String immatriculation;
  private String couleur;
  private int nombreDeSieges;
  private int nombreDePortes;
  private LocalDate dateMiseEnCirculation;
  private Integer puissanceFiscale;
  private BigDecimal valeurNeuf;
  private String deviseMontant;

  public void setValeurNeuf(Valeur valeur) {
    this.valeurNeuf = valeur.getMontant();
    this.deviseMontant = valeur.getDevise();
  }

  public Valeur getValeurNeuf() {
    return Valeur.of(this.valeurNeuf, this.deviseMontant);
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "categorie_id")
  private CategorieVehiculeEntity categorie;

  @OneToMany(mappedBy = "vehicule", fetch = FetchType.LAZY)
  private List<SouscriptionEntity> souscriptions = new ArrayList<>();
}
