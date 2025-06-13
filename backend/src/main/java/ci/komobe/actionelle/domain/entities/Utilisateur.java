package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.exceptions.UtilisateurErreur;
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

  public void modifieMotPasse(String nouveauMotPasse, String nouveauMotPasseCrypte) {
    String nouveauMotPasseLowerCase = nouveauMotPasse.toLowerCase();

    // Règle métier : le mot de passe ne doit pas contenir le nom d'utilisateur
    if (nouveauMotPasseLowerCase.contains(this.username.toLowerCase())) {
      throw new UtilisateurErreur("Le mot de passe ne doit pas contenir votre nom d'utilisateur");
    }

    // Règle métier : interdire certains patterns selon le rôle
    if (nouveauMotPasseLowerCase.contains(this.role.name().toLowerCase())) {
      throw new UtilisateurErreur("Le mot de passe ne peut pas contenir votre rôle");
    }

    this.password = nouveauMotPasseCrypte;
  }
}
