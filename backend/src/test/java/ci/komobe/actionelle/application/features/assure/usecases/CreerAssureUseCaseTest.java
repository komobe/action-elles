package ci.komobe.actionelle.application.features.assure.usecases;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ci.komobe.actionelle.application.features.assure.commands.CreerAssureCommand;
import ci.komobe.actionelle.domain.exceptions.AssureErreur;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreerAssureUseCaseTest {

  @Mock
  private AssureRepository assureRepository;

  private CreerAssureUseCase useCase;

  @BeforeEach
  void setUp() {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    useCase = new CreerAssureUseCase(assureRepository, validator);
  }

  @Test
  @DisplayName("Test que la création d'un assuré échoue car email existe déjà")
  void creationAssureEchoueCarEmailExiste() {
    // Given
    var email = "lea.boucher@exemple.ci";
    var command = CreerAssureCommand.builder()
        .numeroCarteIdentite("CI4839201745")
        .nom("Boucher")
        .prenoms("Léa")
        .sexe("Féminin")
        .dateNaissance(LocalDate.of(1988, 7, 14))
        .lieuNaissance("Abidjan")
        .email(email)
        .telephone("+2250742362857")
        .adresse("Abidjan Yopougon")
        .build();

    when(assureRepository.existeParEmail(email)).thenReturn(true);

    // When & Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(AssureErreur.class)
        .hasMessage("Un assuré avec l'email " + email + " existe déjà");

    verify(assureRepository, never()).enregistrer(any());
  }

  @Test
  @DisplayName("Test que la création d'un assuré se fait avec succès")
  void creationAssureSeFaitAvecSucces() {
    // Given
    var command = CreerAssureCommand.builder()
        .numeroCarteIdentite("CI4839201745")
        .nom("Boucher")
        .prenoms("Léa")
        .sexe("Féminin")
        .dateNaissance(LocalDate.of(1988, 7, 14))
        .lieuNaissance("Abidjan")
        .email("lea.boucher@exemple.ci")
        .telephone("+2250742362857")
        .adresse("Abidjan Yopougon")
        .build();

    when(assureRepository.existeParEmail(command.getEmail())).thenReturn(false);

    // When
    useCase.execute(command);

    // Then
    verify(assureRepository).enregistrer(any());
  }
}