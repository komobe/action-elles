package ci.komobe.actionelle.application.features.assure.usecases;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.exceptions.AssureErreur;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SupprimerAssureUseCaseTest {

  @Mock
  private AssureRepository assureRepository;

  private SupprimerAssureUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new SupprimerAssureUseCase(assureRepository);
  }

  @Test
  @DisplayName("Test que la suppression d'un assuré échoue car assuré non trouvé")
  void suppressionAssureEchoueCarAssureNonTrouve() {
    // Given
    String id = "non-trouver";
    when(assureRepository.chercherParId(id)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> useCase.execute(id))
        .isInstanceOf(AssureErreur.class)
        .hasMessage("Assuré non trouvé");

    verify(assureRepository, never()).supprimer(any(Assure.class));
  }

  @Test
  @DisplayName("Test que la suppression d'un assuré se fait avec succès")
  void suppressionAssureSeFaitAvecSucces() {
    // Given
    var assureId = IdGenerator.generateId();
    var assure = Assure.builder()
        .id(assureId)
        .nom("Doe")
        .prenoms("John")
        .email("john.doe@example.com")
        .telephone("+225 0123456789")
        .dateNaissance(LocalDate.of(1990, 1, 1))
        .lieuNaissance("Abidjan")
        .build();

    when(assureRepository.chercherParId(assureId)).thenReturn(Optional.of(assure));

    // When
    useCase.execute(assureId);

    // Then
    verify(assureRepository).supprimer(any(Assure.class));
  }
}