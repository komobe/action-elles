package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.application.commons.providers.PasswordProvider;
import ci.komobe.actionelle.application.features.utilisateur.commands.InscriptionUtilisateurCommand;
import ci.komobe.actionelle.application.features.utilisateur.usecases.InscrireUtilisateur;
import ci.komobe.actionelle.application.features.utilisateur.usecases.ListerUtilisateurs;
import ci.komobe.actionelle.application.features.utilisateur.usecases.RecupererUtilisateurParUsername;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.PageResponse;
import ci.komobe.actionelle.domain.utils.paginate.PaginationParams;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des utilisateurs
 * 
 * @author Moro KONÉ 2025-05-30
 */
@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
@Tag(name = "Utilisateurs", description = "API de gestion des utilisateurs")
public class UtilisateurController {
  private final UtilisateurRepository utilisateurRepository;
  private final PasswordProvider passwordProvider;

  @PostMapping("/inscrire")
  public void inscrireUtilisateur(InscriptionUtilisateurCommand command) {
    var useCase = new InscrireUtilisateur(utilisateurRepository, passwordProvider);
    useCase.executer(command);
  }

  /**
   * Liste les utilisateurs avec pagination
   * 
   * @param params  paramètres de pagination
   * @param request requête HTTP
   * @return réponse paginée
   */
  @GetMapping
  public PageResponse<Utilisateur> listUtilisateurs(
      @ModelAttribute PaginationParams params,
      HttpServletRequest request
  ) {

    Page<Utilisateur> page = new ListerUtilisateurs(utilisateurRepository)
        .executer(params.toPageRequest());

    return PageResponse.from(page, request.getRequestURL().toString());
  }

  @GetMapping("/profile")
  public Utilisateur recupererProfil(Authentication authentication) {
    String username = authentication.getName();
    return new RecupererUtilisateurParUsername(utilisateurRepository).executer(username);
  }
}
