package ci.komobe.actionelle.application.features.vehicule.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ci.komobe.actionelle.application.features.vehicule.CategorieVehiculeException;
import ci.komobe.actionelle.application.features.vehicule.VehiculeErreur;
import ci.komobe.actionelle.application.features.vehicule.commands.CreerVehiculeCommand;
import ci.komobe.actionelle.application.features.vehicule.commands.ModifierVehiculeCommand;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.repositories.InMemoryCategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.InMemoryVehiculeRepository;
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

  @BeforeEach
  void setUp() {
    vehiculeRepository = new InMemoryVehiculeRepository(NUMBER_VEHICULES_GENERATING);
    var categorieVehiculeRepository = new InMemoryCategorieVehiculeRepository();
    useCase = new ModifierVehiculeUseCase(vehiculeRepository, categorieVehiculeRepository);
  }

  @Test
  @DisplayName("Echec de la modification d'un véhicule car l'immatriculation n'existe pas")
  void modificationVehiculeEchecCarCategorieInexistant() {
    // Given
    String categorieCode = "notfound";
    var vehiculeData = CreerVehiculeCommand.builder()
        .dateMiseEnCirculation(LocalDate.of(2024, 1, 1))
        .immatriculation("123456789012345")
        .couleur("Noir")
        .nombreDeSieges(6)
        .nombreDePortes(5)
        .categorieCode(categorieCode)
        .build();

    var command = ModifierVehiculeCommand.builder()
        .id("123456789012345")
        .vehiculeData(vehiculeData)
        .build();

    // When
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(CategorieVehiculeException.class)
        .hasMessage("La catégorie de véhicule " + categorieCode + " n'existe pas");

    // Then
    assertThat(vehiculeRepository.getSaveCallCount()).isZero();
    assertThat(vehiculeRepository.lister()).hasSize(NUMBER_VEHICULES_GENERATING);
    assertThat(vehiculeRepository.chercherParImmatriculation("123456789012345")).isEmpty();
  }

  @Test
  @DisplayName("Echec de la modification d'un véhicule car l'immatriculation n'existe pas")
  void modificationVehiculeEchecCarImmatriculationInexistant() {
    // Given
    var vehiculeData = CreerVehiculeCommand.builder()
        .dateMiseEnCirculation(LocalDate.of(2024, 1, 1))
        .immatriculation("123456789012345")
        .couleur("Noir")
        .nombreDeSieges(6)
        .nombreDePortes(5)
        .categorieCode("202")
        .build();

    var command = ModifierVehiculeCommand.builder()
        .id("123456789012345")
        .vehiculeData(vehiculeData)
        .build();

    // When
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(VehiculeErreur.class)
        .hasMessage("Le véhicule avec l'immatriculation 123456789012345 n'existe pas");

    // Then
    assertThat(vehiculeRepository.getSaveCallCount()).isZero();
    assertThat(vehiculeRepository.lister()).hasSize(NUMBER_VEHICULES_GENERATING);
    assertThat(vehiculeRepository.chercherParImmatriculation("123456789012345")).isEmpty();
  }

  @Test
  @DisplayName("Modification véhicule réalisé avec succes")
  void modificationVehiculeReussi() {
    var vehicule = vehiculeRepository.findRandomVehicule();

    // Given
    String immatriculation = vehicule.getImmatriculation();
    var vehiculeData = CreerVehiculeCommand.builder()
        .dateMiseEnCirculation(LocalDate.of(2024, 1, 1))
        .immatriculation(immatriculation)
        .couleur("Noir")
        .nombreDeSieges(6)
        .nombreDePortes(5)
        .categorieCode("202")
        .build();

    var command = ModifierVehiculeCommand.builder()
        .id(immatriculation)
        .vehiculeData(vehiculeData)
        .build();

    // When
    useCase.execute(command);

    // Then
    Vehicule vehiculeModifie = vehiculeRepository
        .chercherParImmatriculation(immatriculation)
        .orElse(new Vehicule());

    assertThat(vehiculeModifie.getCouleur()).isEqualTo("Noir");
    assertThat(vehiculeModifie.getNombreDeSieges()).isEqualTo(6);
    assertThat(vehiculeModifie.getNombreDePortes()).isEqualTo(5);
    assertThat(vehiculeModifie.getCategorie().getCode()).isEqualTo("202");
  }
}
