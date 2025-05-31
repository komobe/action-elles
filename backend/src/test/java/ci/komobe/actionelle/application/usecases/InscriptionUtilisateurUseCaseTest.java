package ci.komobe.actionelle.application.usecases;

import ci.komobe.actionelle.application.features.utilisateur.commands.InscriptionUtilisateurCommand;
import ci.komobe.actionelle.application.features.utilisateur.UtilisateurError;
import ci.komobe.actionelle.application.features.utilisateur.usecases.InscriptionUtilisateurUseCase;
import ci.komobe.actionelle.application.commons.providers.PasswordProvider;
import ci.komobe.actionelle.application.repositories.InMemoryUtilisateurRepository;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.application.utils.PlainPassword;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.valueobjects.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InscriptionUtilisateurUseCaseTest {

  private UtilisateurRepository utilisateurRepository;
  private PasswordProvider passwordProvider;
  private InscriptionUtilisateurUseCase useCase;

  @BeforeEach
  void setUp() {
    this.utilisateurRepository = new InMemoryUtilisateurRepository();
    this.passwordProvider = new PlainPassword();
    this.useCase = new InscriptionUtilisateurUseCase(utilisateurRepository, passwordProvider);
  }

  @Test
  @DisplayName("L'inscription d'un utilisateur échoue car le username est déjà utilisé")
  void inscriptionUtilisateurEchoueCarLeUsernameEstDejaUtilise() {
    // When
    String usernameInput = "gabriella";
    var command = new InscriptionUtilisateurCommand(usernameInput, "autrepassword");
    Assertions.assertThrows(UtilisateurError.class, () -> useCase.execute(command));

    // Then
    Assertions.assertTrue(utilisateurRepository.existsByUsername(usernameInput));
  }

  @Test
  @DisplayName("L'inscription d'un utilisateur réussit")
  void inscriptionUtilisateurReussit() {
    // When
    var inscriptionUtilisateurUseCase = new InscriptionUtilisateurUseCase(utilisateurRepository,
        passwordProvider);

    String usernameInput = "armandine";
    String passwordInput = "monsupermotdepasse";
    var command = new InscriptionUtilisateurCommand(usernameInput, passwordInput);
    inscriptionUtilisateurUseCase.execute(command);

    // Then
    Assertions.assertTrue(utilisateurRepository.existsByUsername(usernameInput));
    Utilisateur utilisateurCreated = utilisateurRepository.findByUsername(usernameInput).get();
    Assertions.assertEquals(usernameInput, utilisateurCreated.getUsername());
    Assertions.assertEquals(passwordInput, utilisateurCreated.getPassword());
    Assertions.assertEquals(Role.DEFAULT, utilisateurCreated.getRole());
  }
}
