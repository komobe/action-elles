package ci.komobe.actionelle.application.features.utilisateur.commands;

import ci.komobe.actionelle.domain.valueobjects.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Moro KONÉ 2025-06-03
 */
@Data
public class ModifierUtilisateurCommand {

  @NotBlank(message = "L'identifiant est requis")
  private String id;

  @NotBlank(message = "Le nom d'utilisateur est requis")
  private String username;

  @NotNull(message = "Le rôle est requis")
  private Role role;
}
