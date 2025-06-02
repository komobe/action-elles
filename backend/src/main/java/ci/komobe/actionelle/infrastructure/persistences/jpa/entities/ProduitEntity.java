package ci.komobe.actionelle.infrastructure.persistences.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@NoArgsConstructor
@Entity
@Table(name = "produits")
public class ProduitEntity {

  @Id
  private String id;
  private String code;
  private String nom;
  private String description;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "produit_garanties",
      joinColumns = @JoinColumn(name = "produit_id"),
      inverseJoinColumns = @JoinColumn(name = "garantie_id")
  )
  private List<GarantieEntity> garanties = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "produit_categorie_vehicules",
      joinColumns = @JoinColumn(name = "produit_id"),
      inverseJoinColumns = @JoinColumn(name = "categorie_vehicule_id")
  )
  private List<CategorieVehiculeEntity> categorieVehicules = new ArrayList<>();

  @OneToMany(mappedBy = "produit", fetch = FetchType.LAZY)
  private List<DevisEntity> simulationsDevis = new ArrayList<>();

  @OneToMany(mappedBy = "produit", fetch = FetchType.LAZY)
  private List<SouscriptionEntity> souscriptions = new ArrayList<>();
}

