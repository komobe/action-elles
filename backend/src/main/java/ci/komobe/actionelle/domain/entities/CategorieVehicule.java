package ci.komobe.actionelle.domain.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-28
 */
@Setter
@Getter
@Builder
@EqualsAndHashCode(of = { "id" })
public class CategorieVehicule {

  private String id;
  private String code;
  private String libelle;
  private String description;
}
