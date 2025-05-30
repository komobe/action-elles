package ci.komobe.actionelle.application.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author Moro KONÉ 2025-05-30
 */
public record InscriptionUtilisateurCommand(
    @NotBlank(message = "Le nom d'utilisateur est requis")
    String username,

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 8, max = 100, message = "Le mot de passe doit contenir entre 8 et 100 caractères")
    String password
) {}
