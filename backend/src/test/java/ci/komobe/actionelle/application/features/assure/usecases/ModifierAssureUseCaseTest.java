package ci.komobe.actionelle.application.features.assure.usecases;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ci.komobe.actionelle.application.features.assure.commands.CreerAssureCommand;
import ci.komobe.actionelle.application.features.assure.commands.ModifierAssureCommand;
import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.exceptions.AssureErreur;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ModifierAssureUseCaseTest {

  @Mock
  private AssureRepository assureRepository;

  private ModifierAssureUseCase useCase;

  @BeforeEach
  void setUp() {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    useCase = new ModifierAssureUseCase(assureRepository, validator);
  }

  @Test
  @DisplayName("Test que la modification d'un assuré échoue car assuré non trouvé")
  void modificationAssureEchoueCarAssureNonTrouve() {
    // Given
    var assureId = IdGenerator.generateId();
    var assureData = CreerAssureCommand.builder()
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

    var command = ModifierAssureCommand.builder()
        .id(assureId)
        .assureData(assureData)
        .build();

    when(assureRepository.chercherParId(command.getId())).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(AssureErreur.class)
        .hasMessage("Assuré non trouvé");

    verify(assureRepository, never()).enregistrer(any());
  }

  @Test
  @DisplayName("Test que la modification d'un assuré échoue car email existe déjà")
  void modificationAssureEchoueCarEmailExiste() {
    // Given
    var assureId = IdGenerator.generateId();
    var assureData = CreerAssureCommand.builder()
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

    var command = ModifierAssureCommand.builder()
        .id(assureId)
        .assureData(assureData)
        .build();

    var assure = Assure.builder()
        .id(assureId)
        .numeroCarteIdentite("CI4839201745")
        .nom("Boucher")
        .prenoms("Léa")
        .sexe("Féminin")
        .dateNaissance(LocalDate.of(1988, 7, 14))
        .lieuNaissance("Abidjan")
        .email("old.email@exemple.ci")
        .telephone("+2250742362857")
        .adresse("Abidjan Yopougon")
        .build();

    when(assureRepository.chercherParId(command.getId())).thenReturn(Optional.of(assure));
    when(assureRepository.existeParEmail(assureData.getEmail())).thenReturn(true);

    // When & Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(AssureErreur.class)
        .hasMessage("Un assuré avec l'email " + assureData.getEmail() + " existe déjà");

    verify(assureRepository, never()).enregistrer(any());
  }

  @Test
  @DisplayName("Test que la modification d'un assuré se fait avec succès")
  void modificationAssureSeFaitAvecSucces() {
    // Given
    var assureId = IdGenerator.generateId();
    var assureData = CreerAssureCommand.builder()
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

    var command = ModifierAssureCommand.builder()
        .id(assureId)
        .assureData(assureData)
        .build();

    var assure = Assure.builder()
        .id(assureId)
        .numeroCarteIdentite("CI4839201745")
        .nom("Boucher")
        .prenoms("Léa")
        .sexe("Féminin")
        .dateNaissance(LocalDate.of(1988, 7, 14))
        .lieuNaissance("Abidjan")
        .email("old.email@exemple.ci")
        .telephone("+2250742362857")
        .adresse("Abidjan Yopougon")
        .build();

    when(assureRepository.chercherParId(command.getId())).thenReturn(Optional.of(assure));
    when(assureRepository.existeParEmail(assureData.getEmail())).thenReturn(false);

    // When
    useCase.execute(command);

    // Then
    verify(assureRepository).enregistrer(assure);
  }
}