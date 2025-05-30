package ci.komobe.actionelle.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        new InMemoryCategorieVehiculeRepository());
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
    assertNotNull(souscription);
    Vehicule vehiculeSaved = souscription.getVehicule();
    assertNotNull(vehiculeSaved);

    Assure assureSaved = souscription.getAssure();
    assertNotNull(assureSaved);
    assertEquals(vehicule.getNumeroImmatriculation(), vehiculeSaved.getNumeroImmatriculation());
    assertEquals(assure.getNumeroCarteIdentite(), assureSaved.getNumeroCarteIdentite());
    assertEquals(vehicule.getCouleur(), vehiculeSaved.getCouleur());
    assertEquals(vehicule.getNombreDeSieges(), vehiculeSaved.getNombreDeSieges());
    assertEquals(vehicule.getNombreDePortes(), vehiculeSaved.getNombreDePortes());
    assertEquals(vehicule.getCategorieCode(), vehiculeSaved.getCategorie().getCode());
    assertEquals(vehicule.getDateMiseEnCirculation(), vehiculeSaved.getDateMiseEnCirculation());
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
        .categorieCode("getCode-inexistant")
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
    assertThrows(VehiculeError.class, () -> useCase.execute(command));

    // Then
    var souscriptions = souscriptionRepository.findAll();
    assertTrue(souscriptions.isEmpty());
    var vehicules = vehiculeRepository.findAll();
    assertEquals(NUMBER_VEHICULES_GENERATING, vehicules.size());
    var assures = assureRepository.findAll();
    assertTrue(assures.isEmpty());
  }
}
