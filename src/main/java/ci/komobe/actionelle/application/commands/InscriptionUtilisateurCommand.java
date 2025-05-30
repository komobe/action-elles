package ci.komobe.actionelle.application.commands;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Moro KONÉ 2025-05-30
 */
public record InscriptionUtilisateurCommand(
    @NotBlank()
    String username,
    @NotBlank()
    String password
) {}
