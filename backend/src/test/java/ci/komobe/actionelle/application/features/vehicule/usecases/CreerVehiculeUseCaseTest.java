package ci.komobe.actionelle.application.features.vehicule.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ci.komobe.actionelle.application.features.vehicule.VehiculeErreur;
import ci.komobe.actionelle.application.features.vehicule.commands.CreerVehiculeCommand;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.repositories.InMemoryVehiculeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Moro KONÉ 2025-05-28
 */
@DisplayName("CreerVehiculeUseCase")
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
  @DisplayName("La création d'un véhicule échoue car l'immatriculation existe déjà")
  void creationVehiculeEchoueCarImmatriculationExiste() {
    // Given
    String immatriculation = vehiculeRepository.findAnyImmatriculation();
    var command = CreerVehiculeCommand.builder()
        .dateMiseEnCirculation(LocalDate.of(2024, 1, 1))
        .immatriculation(immatriculation)
        .couleur("Rouge")
        .nombreDeSieges(5)
        .nombreDePortes(4)
        .categorieCode("201")
        .puissanceFiscale(10)
        .valeurNeuf(BigDecimal.valueOf(10_000_000))
        .build();

    // When & Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(VehiculeErreur.class)
        .hasMessage("Un véhicule immatriculé " + immatriculation + " déjà enregistré");

    assertThat(vehiculeRepository.getSaveCallCount()).isZero();
    assertThat(vehiculeRepository.lister()).hasSize(NUMBER_VEHICULES_GENERATING);
  }

  @ParameterizedTest
  @ValueSource(ints = { -1, 0, 101 })
  @DisplayName("La création échoue avec une puissance fiscale invalide")
  void creationEchoueAvecPuissanceFiscaleInvalide(int puissanceInvalide) {
    // Given
    var command = CreerVehiculeCommand.builder()
        .dateMiseEnCirculation(LocalDate.of(2024, 1, 1))
        .immatriculation("123456789012346")
        .couleur("Rouge")
        .nombreDeSieges(5)
        .nombreDePortes(4)
        .categorieCode("201")
        .puissanceFiscale(puissanceInvalide)
        .valeurNeuf(BigDecimal.valueOf(10_000_000))
        .build();

    // When & Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(VehiculeErreur.class)
        .hasMessage("La puissance fiscale doit être comprise entre 1 et 100");
  }

  @Test
  @DisplayName("La création d'un véhicule se fait avec succès")
  void creationVehiculeSeFaitAvecSucces() {
    // Given
    LocalDate dateMiseEnCirculation = LocalDate.of(2024, 1, 1);
    String immatriculation = "123456789012346";
    String couleur = "Rouge";
    int nombreDeSieges = 5;
    int nombreDePortes = 4;
    String categorieCode = "201";
    int puissanceFiscale = 10;
    BigDecimal valeurNeuf = BigDecimal.valueOf(10_000_000);

    var command = CreerVehiculeCommand.builder()
        .dateMiseEnCirculation(dateMiseEnCirculation)
        .immatriculation(immatriculation)
        .couleur(couleur)
        .nombreDeSieges(nombreDeSieges)
        .nombreDePortes(nombreDePortes)
        .categorieCode(categorieCode)
        .puissanceFiscale(puissanceFiscale)
        .valeurNeuf(valeurNeuf)
        .build();

    // When
    useCase.execute(command);

    // Then
    assertThat(vehiculeRepository.getSaveCallCount()).isEqualTo(1);
    assertThat(vehiculeRepository.lister()).hasSize(NUMBER_VEHICULES_GENERATING + 1);

    Vehicule vehiculeCreated = vehiculeRepository.chercherParImmatriculation(immatriculation)
        .orElseThrow(() -> new AssertionError("Le véhicule devrait exister"));

    assertThat(vehiculeCreated)
        .isNotNull()
        .satisfies(vehicule -> {
          assertThat(vehicule.getImmatriculation()).isEqualTo(immatriculation);
          assertThat(vehicule.getCouleur()).isEqualTo(couleur);
          assertThat(vehicule.getNombreDeSieges()).isEqualTo(nombreDeSieges);
          assertThat(vehicule.getNombreDePortes()).isEqualTo(nombreDePortes);
          assertThat(vehicule.getPuissanceFiscale()).isEqualTo(puissanceFiscale);
          assertThat(vehicule.getValeurNeuf().getMontant()).isEqualByComparingTo(valeurNeuf);
          assertThat(vehicule.getDateMiseEnCirculation()).isEqualTo(dateMiseEnCirculation);
          assertThat(vehicule.getId()).isNotNull();
        });
  }
}
