package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.application.commons.providers.PasswordProvider;
import ci.komobe.actionelle.application.features.utilisateur.commands.InscriptionUtilisateurCommand;
import ci.komobe.actionelle.application.features.utilisateur.commands.ModifierMotPasseCommand;
import ci.komobe.actionelle.application.features.utilisateur.commands.ModifierUtilisateurCommand;
import ci.komobe.actionelle.application.features.utilisateur.usecases.InscrireUtilisateur;
import ci.komobe.actionelle.application.features.utilisateur.usecases.ListerUtilisateurs;
import ci.komobe.actionelle.application.features.utilisateur.usecases.ModifierMotPasseUseCase;
import ci.komobe.actionelle.application.features.utilisateur.usecases.ModifierUtilisateurUseCase;
import ci.komobe.actionelle.application.features.utilisateur.usecases.RecupererUtilisateurParUsername;
import ci.komobe.actionelle.application.features.utilisateur.usecases.SupprimerUtilisateurUseCase;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.PageResponse;
import ci.komobe.actionelle.domain.utils.paginate.PaginationParams;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des utilisateurs
 *
 * @author Moro KONÉ 2025-05-30
 */
@RestController
@RequestMapping("/api/v1/utilisateurs")
@RequiredArgsConstructor
@Tag(name = "Utilisateurs", description = "API de gestion des utilisateurs")
public class UtilisateurController {

  private final UtilisateurRepository utilisateurRepository;
  private final PasswordProvider passwordProvider;

  @GetMapping
  public PageResponse<Utilisateur> listerUtilisateurs(
      @Valid @ModelAttribute PaginationParams paginationParams,
      HttpServletRequest request) {
    var useCase = new ListerUtilisateurs(utilisateurRepository);
    Page<Utilisateur> page = useCase.executer(paginationParams.toPageRequest());
    return PageResponse.from(page, request.getRequestURL().toString());
  }

  @GetMapping("/{username}")
  public Utilisateur recupererUtilisateur(@PathVariable String username) {
    var useCase = new RecupererUtilisateurParUsername(utilisateurRepository);
    return useCase.executer(username);
  }

  @PostMapping
  public void inscrireUtilisateur(InscriptionUtilisateurCommand command) {
    var useCase = new InscrireUtilisateur(utilisateurRepository, passwordProvider);
    useCase.executer(command);
  }

  @PutMapping
  public void modifierUtilisateur(@Valid @RequestBody ModifierUtilisateurCommand command) {
    var useCase = new ModifierUtilisateurUseCase(utilisateurRepository);
    useCase.execute(command);
  }

  @PutMapping("/reset-password")
  public void modifierMotDePasseUtilisateur(
      @Valid @RequestBody ModifierMotPasseCommand command) {
    var useCase = new ModifierMotPasseUseCase(utilisateurRepository, passwordProvider);
    useCase.execute(command);
  }

  @DeleteMapping("/{id}")
  public void supprimerUtilisateur(@PathVariable String id) {
    var useCase = new SupprimerUtilisateurUseCase(utilisateurRepository);
    useCase.execute(id);
  }
}
