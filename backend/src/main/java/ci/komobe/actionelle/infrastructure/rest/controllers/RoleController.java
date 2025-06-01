package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.domain.valueobjects.Role;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Moro KONÉ 2025-05-31
 */
@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Rôles", description = "API de gestion des rôles utilisateurs")
public class RoleController {

  @GetMapping
  public Collection<Role> listRoles() {
    return Arrays.stream(Role.values()).toList();
  }
}
