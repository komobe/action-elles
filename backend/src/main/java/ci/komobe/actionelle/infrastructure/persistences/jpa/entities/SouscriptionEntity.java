package ci.komobe.actionelle.infrastructure.persistences.jpa.entities;

import ci.komobe.actionelle.domain.valueobjects.StatutSouscription;
import ci.komobe.actionelle.domain.valueobjects.Valeur;
import ci.komobe.actionelle.infrastructure.persistences.jpa.converters.ValeurConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
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
@Table(name = "souscriptions")
public class SouscriptionEntity {

  @Id
  private String id;
  private String numero;
  private LocalDate dateSouscription;

  @Enumerated(EnumType.STRING)
  private StatutSouscription statut;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "assure_id", nullable = false)
  private AssureEntity assure;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "vehicule_id", nullable = false)
  private VehiculeEntity vehicule;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "produit_id", nullable = false)
  private ProduitEntity produit;

  @Convert(converter = ValeurConverter.class)
  @Column(name = "vehicule_valeur_venale", nullable = false)
  private Valeur vehiculeValeurVenale;
}
