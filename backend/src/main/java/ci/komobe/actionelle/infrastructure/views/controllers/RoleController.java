package ci.komobe.actionelle.infrastructure.views.controllers;

import ci.komobe.actionelle.domain.valueobjects.Role;
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
public class RoleController {

  @GetMapping
  public Collection<Role> listRoles() {
    return Arrays.stream(Role.values()).toList();
  }
}
