package ci.komobe.actionelle.infrastructure.hibernatejpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@Entity
@NoArgsConstructor
@Table(name = "assures")
public class AssureEntityJpa {

  @Id
  private String id;
  private String adresse;
  private String telephone;
  private String nom;
  private String prenom;
  private String numeroCarteIdentite;
  private String ville;

  @OneToMany(mappedBy = "assure", fetch = FetchType.LAZY)
  private List<SouscriptionEntityJpa> souscriptions = new ArrayList<>();
}
