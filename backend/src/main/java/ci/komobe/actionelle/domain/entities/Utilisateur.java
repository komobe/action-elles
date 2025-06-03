package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.valueobjects.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité Utilisateur du domaine
 *
 * @author Moro KONÉ 2025-05-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

  private String id;
  private String username;
  private String password;
  private Role role;

  public void inscrire(String username, String password) {
    this.username = username;
    this.password = password;
    this.role = Role.DEFAULT;
  }

  public void modifier(String username, Role role) {
    this.username = username;
    this.role = role;
  }

  public void modifieMotPasse(String nouveauMotPasse) {
    this.password = nouveauMotPasse;
  }
}
