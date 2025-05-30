package ci.komobe.actionelle.domain.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-28
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"id"})
public class Assure {

  private String id;
  private String adresse;
  private String telephone;
  private String nom;
  private String prenom;
  private String numeroCarteIdentite;
  private String ville;
}
