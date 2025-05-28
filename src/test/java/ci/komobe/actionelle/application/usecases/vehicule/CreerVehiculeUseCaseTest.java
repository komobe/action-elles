package ci.komobe.actionelle.application.usecases.vehicule;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import ci.komobe.actionelle.application.commands.vehicule.CreerVehiculeCommand;
import ci.komobe.actionelle.application.exceptions.VehiculeError;
import ci.komobe.actionelle.application.repositories.InMemoryVehiculeRepository;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Moro KONÉ 2025-05-28
 */
class CreerVehiculeUseCaseTest {

  private static final int NUMBER_VEHICULES_GENERATING = 10;
  private CreerVehiculeUseCase useCase;
  private InMemoryVehiculeRepository vehiculeRepository;

  @BeforeEach
  void setUp() {
    vehiculeRepository = new InMemoryVehiculeRepository(NUMBER_VEHICULES_GENERATING);
    useCase = new CreerVehiculeUseCase(vehiculeRepository);
  }

  @Test
  @DisplayName("Test que la création d'un véhicule échoue car immatriculation existe déjà")
  void creationVehiculeEchoueCarImmatriculationExiste() {
    String immatriculation = vehiculeRepository.findAnyImmatriculation();

    // Given
    var command = CreerVehiculeCommand.builder()
        .dateMiseEnCirculation(LocalDate.of(2024, 1, 1))
        .numeroImmatriculation(immatriculation)
        .couleur("Rouge")
        .nombreDeSieges(5)
        .nombreDePortes(4)
        .categorieCode("201")
        .build();

    // When
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(VehiculeError.class)
        .hasMessage("Un véhicule immatriculé " + immatriculation + " déjà enregistré");

    // Then
    assertThat(vehiculeRepository.getSaveCallCount()).isZero();
  }

  @Test
  @DisplayName("Test que la création d'un véhicule se fait avec succès")
  void creationVehiculeSeFaitAvecSucces() {
    // Given
    var command = CreerVehiculeCommand.builder()
        .dateMiseEnCirculation(LocalDate.of(2024, 1, 1))
        .numeroImmatriculation("123456789012346")
        .couleur("Rouge")
        .nombreDeSieges(5)
        .nombreDePortes(4)
        .categorieCode("201")
        .build();

    // When
    useCase.execute(command);

    // Then
    int vehiculeSize = vehiculeRepository.findAll().size();

    assertThat(vehiculeRepository.getSaveCallCount()).isEqualTo(1);
    assertThat(vehiculeSize).isEqualTo(NUMBER_VEHICULES_GENERATING + 1);
  }
}
