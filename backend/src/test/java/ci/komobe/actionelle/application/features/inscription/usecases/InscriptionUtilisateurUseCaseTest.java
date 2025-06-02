package ci.komobe.actionelle.application.features.inscription.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ci.komobe.actionelle.application.commons.PlainPassword;
import ci.komobe.actionelle.application.commons.providers.PasswordProvider;
import ci.komobe.actionelle.application.features.utilisateur.commands.InscriptionUtilisateurCommand;
import ci.komobe.actionelle.application.features.utilisateur.usecases.InscrireUtilisateur;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.exceptions.UtilisateurErreur;
import ci.komobe.actionelle.domain.repositories.InMemoryUtilisateurRepository;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.valueobjects.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("InscrireUtilisateur")
class InscriptionUtilisateurUseCaseTest {

  private UtilisateurRepository utilisateurRepository;
  private InscrireUtilisateur inscrireUtilisateur;

  @BeforeEach
  void setUp() {
    this.utilisateurRepository = new InMemoryUtilisateurRepository();
    PasswordProvider passwordProvider = new PlainPassword();
    this.inscrireUtilisateur = new InscrireUtilisateur(utilisateurRepository, passwordProvider);
  }

  @Test
  @DisplayName("L'inscription d'un utilisateur échoue car le username est déjà utilisé")
  void inscriptionUtilisateurEchoueCarLeUsernameEstDejaUtilise() {
    // Given
    String usernameExistant = "gabriella";
    var utilisateurExistant = Utilisateur.builder()
        .username(usernameExistant)
        .password("password123")
        .role(Role.DEFAULT)
        .build();
    utilisateurRepository.enregistrer(utilisateurExistant);

    var command = new InscriptionUtilisateurCommand(usernameExistant, "autrepassword");

    // When & Then
    assertThatThrownBy(() -> inscrireUtilisateur.executer(command))
        .isInstanceOf(UtilisateurErreur.class)
        .hasMessage("Un utilisateur avec le username " + usernameExistant + " existe déjà");

    assertThat(utilisateurRepository.existParUsername(usernameExistant)).isTrue();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   "})
  @DisplayName("L'inscription échoue avec un username invalide")
  void inscriptionEchoueAvecUsernameInvalide(String usernameInvalide) {
    // Given
    var command = new InscriptionUtilisateurCommand(usernameInvalide, "password123");

    // When & Then
    assertThatThrownBy(() -> inscrireUtilisateur.executer(command))
        .isInstanceOf(UtilisateurErreur.class)
        .hasMessage("Le username ne peut pas être vide");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   "})
  @DisplayName("L'inscription échoue avec un mot de passe invalide")
  void inscriptionEchoueAvecMotDePasseInvalide(String passwordInvalide) {
    // Given
    var command = new InscriptionUtilisateurCommand("nouveauuser", passwordInvalide);

    // When & Then
    assertThatThrownBy(() -> inscrireUtilisateur.executer(command))
        .isInstanceOf(UtilisateurErreur.class)
        .hasMessage("Le mot de passe ne peut pas être vide");
  }

  @Test
  @DisplayName("L'inscription d'un utilisateur réussit")
  void inscriptionUtilisateurReussit() {
    // Given
    String usernameInput = "armandine";
    String passwordInput = "monsupermotdepasse";
    var command = new InscriptionUtilisateurCommand(usernameInput, passwordInput);

    // When
    inscrireUtilisateur.executer(command);

    // Then
    assertThat(utilisateurRepository.existParUsername(usernameInput)).isTrue();

    Utilisateur utilisateurCreated = utilisateurRepository.chercherParUsername(usernameInput)
        .orElseThrow(() -> new AssertionError("L'utilisateur devrait exister"));

    assertThat(utilisateurCreated)
        .isNotNull()
        .satisfies(utilisateur -> {
          assertThat(utilisateur.getUsername()).isEqualTo(usernameInput);
          assertThat(utilisateur.getPassword()).isEqualTo(passwordInput);
          assertThat(utilisateur.getRole()).isEqualTo(Role.DEFAULT);
          assertThat(utilisateur.getId()).isNotNull();
        });
  }
}
