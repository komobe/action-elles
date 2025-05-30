package ci.komobe.actionelle.infrastructure.hibernatejpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.embeddables.VehiculeEmbeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Getter
@Setter
@Entity
@Table(name = "devis")
public class DevisJpaEntity {

  @Id
  private String id;

  @Column(name = "reference", nullable = false, unique = true, length = 14)
  private String reference;

  @Column(name = "montant_prime", nullable = false)
  private BigDecimal price;

  @Column(name = "date_simulation", nullable = false)
  private LocalDate dateSimulation;

  @Column(name = "date_expiration", nullable = false)
  private LocalDate dateExpiration;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "produit_id")
  private ProduitEntityJpa produit;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "categorie_id")
  private CategorieVehiculeEntityJpa categorie;

  @Embedded
  private VehiculeEmbeddable vehicule;
}