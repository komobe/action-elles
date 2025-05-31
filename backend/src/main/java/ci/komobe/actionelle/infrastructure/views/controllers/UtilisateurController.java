package ci.komobe.actionelle.infrastructure.views.controllers;

import ci.komobe.actionelle.application.commands.InscriptionUtilisateurCommand;
import ci.komobe.actionelle.application.providers.PasswordProvider;
import ci.komobe.actionelle.application.repositories.UtilisateurRepository;
import ci.komobe.actionelle.application.usecases.InscriptionUtilisateurUseCase;
import ci.komobe.actionelle.application.usecases.queryhandler.GetAllUtilisateurs;
import ci.komobe.actionelle.application.usecases.queryhandler.GetUtilisateurByUsername;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import jakarta.validation.Valid;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Moro KONÉ 2025-05-30
 */
@RestController
@RequestMapping("/api/v1/utilisateurs")
public class UtilisateurController {

  private final UtilisateurRepository utilisateurRepository;
  private final PasswordProvider passwordProvider;

  public UtilisateurController(
      UtilisateurRepository utilisateurRepository,
      PasswordProvider passwordProvider
  ) {
    this.utilisateurRepository = utilisateurRepository;
    this.passwordProvider = passwordProvider;
  }


  @PostMapping("/inscrire")
  public void inscrireUtilisateur(@RequestBody @Valid InscriptionUtilisateurCommand command) {
    var useCase = new InscriptionUtilisateurUseCase(utilisateurRepository, passwordProvider);
    useCase.execute(command);
  }

  @GetMapping
  public Collection<Utilisateur> listUtilisateurs() {
    return new GetAllUtilisateurs(utilisateurRepository).get();
  }

  @GetMapping("/profile")
  public Utilisateur getProfile(Authentication authentication){
    String username = authentication.getName();
    return new GetUtilisateurByUsername(utilisateurRepository).get(username);
  }
}
