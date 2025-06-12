package ci.komobe.actionelle.application.features.vehicule.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ci.komobe.actionelle.domain.exceptions.VehiculeErreur;
import ci.komobe.actionelle.application.features.vehicule.commands.SupprimerVehiculeByImmatriculation;
import ci.komobe.actionelle.domain.repositories.InMemoryVehiculeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Moro KONÉ 2025-05-28
 */
class SupprimerVehiculeUseCaseTest {

  private static final int NUMBER_VEHICULES_GENERATING = 10;
  private SupprimerVehiculeUseCase useCase;
  private InMemoryVehiculeRepository vehiculeRepository;

  @BeforeEach
  void setUp() {
    vehiculeRepository = new InMemoryVehiculeRepository(NUMBER_VEHICULES_GENERATING);
    useCase = new SupprimerVehiculeUseCase(vehiculeRepository);
  }

  @Test
  @DisplayName("Echec de la suppression d'un véhicule car l'immatriculation n'existe pas")
  void suppressionVehiculeEchecCarImmatriculationInexistant() {
    // Given
    var immatriculation = "notfound";
    var command = new SupprimerVehiculeByImmatriculation(immatriculation);

    // When
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(VehiculeErreur.class)
        .hasMessage("Le véhicule est introuvable avec les critères: " + command.field() + " = "
            + command.value());

    // Then
    assertThat(vehiculeRepository.lister()).hasSize(NUMBER_VEHICULES_GENERATING);
  }

  @Test
  @DisplayName("Suppression d'un véhicule réalisé avec succès")
  void suppressionVehiculeReussi() {
    // Given
    String immatriculation = vehiculeRepository.findAnyImmatriculation();
    var command = new SupprimerVehiculeByImmatriculation(immatriculation);

    // When
    useCase.execute(command);

    // Then
    assertThat(vehiculeRepository.lister()).hasSize(NUMBER_VEHICULES_GENERATING - 1);
    assertThat(vehiculeRepository.chercherParSpec(command)).isEmpty();
  }
}
