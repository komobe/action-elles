package ci.komobe.actionelle.application.features.utilisateur.commands;

import jakarta.validation.constraints.NotNull;

/**
 * @author Moro KONÉ 2025-06-03
 */
@lombok.Data
public class ModifierMotPasseCommand {

  @NotNull(message = "L'identifiant est requis")
  private String id;

  @NotNull(message = "Le mot de passe est requis")
  private String newPassword;
}
