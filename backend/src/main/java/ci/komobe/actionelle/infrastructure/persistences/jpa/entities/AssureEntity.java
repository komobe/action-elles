package ci.komobe.actionelle.infrastructure.persistences.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@Table(name = "assures")
public class AssureEntity {

  @Id
  private String id;
  private String nom;
  private String prenoms;
  private LocalDate dateNaissance;
  private String lieuNaissance;
  private String sexe;
  private String numeroCarteIdentite;
  private String email;
  private String telephone;
  private String adresse;

  @OneToMany(mappedBy = "assure", fetch = FetchType.LAZY)
  private List<SouscriptionEntity> souscriptions = new ArrayList<>();
}
