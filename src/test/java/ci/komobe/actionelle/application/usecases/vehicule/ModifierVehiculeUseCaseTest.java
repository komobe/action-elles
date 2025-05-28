package ci.komobe.actionelle.application.usecases.vehicule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ci.komobe.actionelle.application.commands.vehicule.ModifierVehiculeCommand;
import ci.komobe.actionelle.application.exceptions.CategorieVehiculeError;
import ci.komobe.actionelle.application.exceptions.VehiculeError;
import ci.komobe.actionelle.application.repositories.InMemoryCategorieVehiculeRepository;
import ci.komobe.actionelle.application.repositories.InMemoryVehiculeRepository;
import ci.komobe.actionelle.domain.entities.Vehicule;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Moro KONÉ 2025-05-28
 */
class ModifierVehiculeUseCaseTest {

  private static final int NUMBER_VEHICULES_GENERATING = 10;
  private ModifierVehiculeUseCase useCase;
  private InMemoryVehiculeRepository vehiculeRepository;
  private InMemoryCategorieVehiculeRepository categorieVehiculeRepository;

  @BeforeEach
  void setUp() {
    vehiculeRepository = new InMemoryVehiculeRepository(NUMBER_VEHICULES_GENERATING);
    categorieVehiculeRepository = new InMemoryCategorieVehiculeRepository();
    useCase = new ModifierVehiculeUseCase(vehiculeRepository, categorieVehiculeRepository);
  }

  @Test
  @DisplayName("Echec de la modification d'un véhicule car l'immatriculation n'existe pas")
  void modificationVehiculeEchecCarCategorieInexistant() {
    // Given
    String categorieCode = "notfound";
    var command = ModifierVehiculeCommand.builder()
        .dateMiseEnCirculation(LocalDate.of(2024, 1, 1))
        .numeroImmatriculation("123456789012345")
        .couleur("Noir")
        .nombreDeSieges(6)
        .nombreDePortes(5)
        .categorieCode(categorieCode)
        .build();

    // When
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(CategorieVehiculeError.class)
        .hasMessage("La catégorie de véhicule " + categorieCode + " n'existe pas");

    // Then
    assertThat(vehiculeRepository.getSaveCallCount()).isZero();
    assertThat(vehiculeRepository.findAll()).hasSize(NUMBER_VEHICULES_GENERATING);
    assertThat(vehiculeRepository.findByImmatriculation("123456789012345")).isEmpty();
  }

  @Test
  @DisplayName("Echec de la modification d'un véhicule car l'immatriculation n'existe pas")
  void modificationVehiculeEchecCarImmatriculationInexistant() {
    // Given
    var command = ModifierVehiculeCommand.builder()
        .dateMiseEnCirculation(LocalDate.of(2024, 1, 1))
        .numeroImmatriculation("123456789012345")
        .couleur("Noir")
        .nombreDeSieges(6)
        .nombreDePortes(5)
        .categorieCode("202")
        .build();

    // When
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(VehiculeError.class)
        .hasMessage("Le véhicule avec l'immatriculation 123456789012345 n'existe pas");

    // Then
    assertThat(vehiculeRepository.getSaveCallCount()).isZero();
    assertThat(vehiculeRepository.findAll()).hasSize(NUMBER_VEHICULES_GENERATING);
    assertThat(vehiculeRepository.findByImmatriculation("123456789012345")).isEmpty();
  }

  @Test
  @DisplayName("Modification véhicule réalisé avec succes")
  void modificationVehiculeReussi() {
    var vehicule = vehiculeRepository.findRandomVehicule();

    // Given
    String immatriculation = vehicule.getNumeroImmatriculation();
    var command = ModifierVehiculeCommand.builder()
        .dateMiseEnCirculation(LocalDate.of(2024, 1, 1))
        .numeroImmatriculation(immatriculation)
        .couleur("Noir")
        .nombreDeSieges(6)
        .nombreDePortes(5)
        .categorieCode("202")
        .build();

    // When
    useCase.execute(command);

    // Then
    Vehicule vehiculeModifie = vehiculeRepository
        .findByImmatriculation(immatriculation)
        .orElse(new Vehicule());

    assertThat(vehiculeModifie.getCouleur()).isEqualTo("Noir");
    assertThat(vehiculeModifie.getNombreDeSieges()).isEqualTo(6);
    assertThat(vehiculeModifie.getNombreDePortes()).isEqualTo(5);
    assertThat(vehiculeModifie.getCategorie().code()).isEqualTo("202");
  }
}
