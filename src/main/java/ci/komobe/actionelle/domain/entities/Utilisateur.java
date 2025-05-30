package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.valueobjects.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class Utilisateur {

  private String id;
  private String username;
  private String password;
  private Role role;
}
