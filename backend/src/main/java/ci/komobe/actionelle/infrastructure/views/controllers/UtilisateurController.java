package ci.komobe.actionelle.infrastructure.views.controllers;

import ci.komobe.actionelle.application.commands.InscriptionUtilisateurCommand;
import ci.komobe.actionelle.application.providers.PasswordProvider;
import ci.komobe.actionelle.application.repositories.UtilisateurRepository;
import ci.komobe.actionelle.application.usecases.InscriptionUtilisateurUseCase;
import jakarta.validation.Valid;
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
}
