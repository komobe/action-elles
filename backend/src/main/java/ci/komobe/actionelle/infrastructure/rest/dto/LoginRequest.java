package ci.komobe.actionelle.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Moro KONÉ 2025-05-30
 */

public record LoginRequest(
    @NotBlank
    String username,
    @NotBlank
    String password
) {}
