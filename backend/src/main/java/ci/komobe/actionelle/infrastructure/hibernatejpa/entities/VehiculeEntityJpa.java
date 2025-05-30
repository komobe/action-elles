package ci.komobe.actionelle.infrastructure.hibernatejpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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
public class VehiculeEntityJpa {

  @Id
  private String id;

  private LocalDate dateMiseEnCirculation;
  private String numeroImmatriculation;
  private String couleur;
  private int nombreDeSieges;
  private int nombreDePortes;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "categorie_id")
  private CategorieVehiculeEntityJpa categorie;

  @OneToMany(mappedBy = "vehicule", fetch = FetchType.LAZY)
  private List<SouscriptionEntityJpa> souscriptions = new ArrayList<>();
}
