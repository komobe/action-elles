package ci.komobe.actionelle.infrastructure.hibernatejpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

/**
 * @author Moro KONÉ 2025-05-29
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "categorie_vehicules")
public class CategorieVehiculeEntityJpa {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(name = "id", updatable = false, nullable = false, length = 36)
  private String id;

  @Column(length = 50, unique = true, nullable = false)
  private String code;

  @Column(nullable = false, length = 100)
  private String libelle;

  @Column()
  private String description;

  @ManyToMany(mappedBy = "categorieVehicules", fetch = FetchType.LAZY)
  private List<ProduitEntityJpa> produits = new ArrayList<>();

  @OneToMany(mappedBy = "categorie", fetch = FetchType.LAZY)
  private List<VehiculeEntityJpa> vehicules = new ArrayList<>();
}
