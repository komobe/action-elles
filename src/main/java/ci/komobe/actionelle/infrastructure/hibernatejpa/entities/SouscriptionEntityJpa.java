package ci.komobe.actionelle.infrastructure.hibernatejpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
public class SouscriptionEntityJpa {

  @Id
  private String id;

  private String numero;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "assure_id")
  private AssureEntityJpa assure;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "vehicule_id")
  private VehiculeEntityJpa vehicule;
}
