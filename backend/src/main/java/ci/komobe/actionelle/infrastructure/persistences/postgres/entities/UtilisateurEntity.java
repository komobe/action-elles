package ci.komobe.actionelle.infrastructure.persistences.postgres.entities;

import ci.komobe.actionelle.domain.valueobjects.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "utilisateurs")
public class UtilisateurEntity implements UserDetails {

  @Id
  @Column(name = "id", updatable = false, nullable = false, length = 36)
  private String id;
  @Column(name = "nom_utilisateur", nullable = false, unique = true, length = 50)
  private String username;
  @Column(name = "mot_de_passe", nullable = false, length = 100)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }
}
