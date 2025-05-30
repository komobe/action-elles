package ci.komobe.actionelle.application.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ci.komobe.actionelle.application.commands.InscriptionUtilisateurCommand;
import ci.komobe.actionelle.application.exceptions.UtilisateurError;
import ci.komobe.actionelle.application.providers.PasswordProvider;
import ci.komobe.actionelle.application.repositories.InMemoryUtilisateurRepository;
import ci.komobe.actionelle.application.repositories.UtilisateurRepository;
import ci.komobe.actionelle.application.utils.PlainPassword;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.valueobjects.Role;
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
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(UtilisateurError.class)
        .hasMessage("Utilisateur déjà existant");

    // Then
    assertThat(utilisateurRepository.existsByUsername(usernameInput)).isTrue();
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
    assertThat(utilisateurRepository.existsByUsername(usernameInput)).isTrue();
    Utilisateur utilisateurCreated = utilisateurRepository.findByUsername(usernameInput).get();
    assertThat(utilisateurCreated.getUsername()).isEqualTo(usernameInput);
    assertThat(utilisateurCreated.getPassword()).isEqualTo(passwordInput);
    assertThat(utilisateurCreated.getRole()).isEqualTo(Role.DEFAULT);
  }
}
