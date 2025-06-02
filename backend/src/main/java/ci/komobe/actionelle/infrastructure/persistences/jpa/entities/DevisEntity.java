package ci.komobe.actionelle.infrastructure.persistences.jpa.entities;

import ci.komobe.actionelle.domain.valueobjects.Valeur;
import ci.komobe.actionelle.infrastructure.persistences.jpa.converters.ValeurConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Getter
@Setter
@Entity
@Table(name = "devis")
public class DevisEntity {

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

  @Convert(converter = ValeurConverter.class)
  @Column(name = "vehicule_valeur_venale", nullable = false)
  private Valeur vehiculeValeurVenale;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "produit_id")
  private ProduitEntity produit;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "vehicule_id")
  private VehiculeEntity vehicule;
}