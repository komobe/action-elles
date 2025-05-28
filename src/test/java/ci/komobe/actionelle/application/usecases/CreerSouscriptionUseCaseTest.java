package ci.komobe.actionelle.application.usecases;

import ci.komobe.actionelle.application.commands.CreerSouscriptionCommand;
import ci.komobe.actionelle.application.commands.assure.AssureCommandBase;
import ci.komobe.actionelle.application.commands.vehicule.VehiculeCommandBase;
import ci.komobe.actionelle.application.exceptions.VehiculeError;
import ci.komobe.actionelle.application.repositories.AssureRepository;
import ci.komobe.actionelle.application.repositories.InMemoryAssureRepository;
import ci.komobe.actionelle.application.repositories.InMemoryCategorieVehiculeRepository;
import ci.komobe.actionelle.application.repositories.InMemorySouscriptionRepository;
import ci.komobe.actionelle.application.repositories.InMemoryVehiculeRepository;
import ci.komobe.actionelle.application.repositories.SouscriptionRepository;
import ci.komobe.actionelle.application.repositories.VehiculeRepository;
import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.entities.Vehicule;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Moro KONÉ 2025-05-28
 */
class CreerSouscriptionUseCaseTest {

  private static final int NUMBER_VEHICULES_GENERATING = 10;

  private VehiculeRepository vehiculeRepository;
  private AssureRepository assureRepository;
  private SouscriptionRepository souscriptionRepository;
  private CreerSouscriptionUseCase useCase;

  @BeforeEach
  void setUp() {
    vehiculeRepository = new InMemoryVehiculeRepository(NUMBER_VEHICULES_GENERATING);
    assureRepository = new InMemoryAssureRepository();
    souscriptionRepository = new InMemorySouscriptionRepository();
    useCase = new CreerSouscriptionUseCase(
        vehiculeRepository,
        assureRepository,
        souscriptionRepository,
        new InMemoryCategorieVehiculeRepository()
    );
  }

  @Test
  @DisplayName("Créer une souscription avec succès")
  void creerSouscriptionAvecSucces() {
    // Given
    var vehicule = VehiculeCommandBase.builder()
        .dateMiseEnCirculation(LocalDate.of(2020, 1, 1))
        .numeroImmatriculation("786 GJ 01")
        .couleur("Rouge")
        .nombreDeSieges(5)
        .nombreDePortes(5)
        .categorieCode("201")
        .build();

    var assure = AssureCommandBase.builder()
        .adresse("Yopougon Abidjan")
        .telephone("+225 07 XX XX XX XX")
        .nom("John")
        .prenom("Doe")
        .numeroCarteIdentite("CI1234567890")
        .ville("Abidjan")
        .build();

    var command = new CreerSouscriptionCommand(vehicule, assure);

    // When
    useCase.execute(command);

    // Assert
    var souscription = souscriptionRepository.findAll().stream().findFirst().orElse(null);
    Assertions.assertThat(souscription).isNotNull();
    Vehicule vehiculeSaved = souscription.getVehicule();
    Assertions.assertThat(vehiculeSaved).isNotNull();
    Assure assureSaved = souscription.getAssure();
    Assertions.assertThat(assureSaved).isNotNull();

    Assertions
        .assertThat(vehiculeSaved.getNumeroImmatriculation())
        .isEqualTo(vehicule.getNumeroImmatriculation());
    Assertions
        .assertThat(assureSaved.getNumeroCarteIdentite())
        .isEqualTo(assure.getNumeroCarteIdentite());

    Assertions
        .assertThat(vehiculeSaved.getCouleur())
        .isEqualTo(vehicule.getCouleur());

    Assertions
        .assertThat(vehiculeSaved.getNombreDeSieges())
        .isEqualTo(vehicule.getNombreDeSieges());

    Assertions
        .assertThat(vehiculeSaved.getNombreDePortes())
        .isEqualTo(vehicule.getNombreDePortes());

    Assertions
        .assertThat(vehiculeSaved.getCategorie().code())
        .isEqualTo(vehicule.getCategorieCode());

    Assertions
        .assertThat(vehiculeSaved.getDateMiseEnCirculation())
        .isEqualTo(vehicule.getDateMiseEnCirculation());
  }

  @Test
  @DisplayName("Echec de la création d'une souscription car la catégorie de véhicule n'existe pas")
  void echecCreerSouscriptionCarCategorieInexistante() {
    // Given
    var vehicule = VehiculeCommandBase.builder()
        .dateMiseEnCirculation(LocalDate.of(2020, 1, 1))
        .numeroImmatriculation("789 GJ 01")
        .couleur("Rouge")
        .nombreDeSieges(5)
        .nombreDePortes(5)
        .categorieCode("code-inexistant")
        .build();

    var assure = AssureCommandBase.builder()
        .adresse("Yopougon Abidjan")
        .telephone("+225 07 XX XX XX XX")
        .nom("John")
        .prenom("Doe")
        .numeroCarteIdentite("CI1234567890")
        .ville("Abidjan")
        .build();

    var command = new CreerSouscriptionCommand(vehicule, assure);

    // When
    Assertions.assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(VehiculeError.class)
        .hasMessage("La catégorie de véhicule " + vehicule.getCategorieCode() + " n'existe pas");

    // Then
    var souscriptions = souscriptionRepository.findAll();
    Assertions.assertThat(souscriptions).isEmpty();
    var vehicules = vehiculeRepository.findAll();
    Assertions.assertThat(vehicules).hasSize(NUMBER_VEHICULES_GENERATING);
    var assures = assureRepository.findAll();
    Assertions.assertThat(assures).isEmpty();
  }
}
