package ci.komobe.actionelle.application.features.souscription.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ci.komobe.actionelle.application.features.assure.commands.CreerAssureCommand;
import ci.komobe.actionelle.application.features.souscription.commands.CreerSouscriptionCommand;
import ci.komobe.actionelle.application.features.vehicule.commands.CreerVehiculeCommand;
import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.exceptions.ProduitErreur;
import ci.komobe.actionelle.domain.repositories.AssureRepository;
import ci.komobe.actionelle.domain.repositories.InMemoryAssureRepository;
import ci.komobe.actionelle.domain.repositories.InMemoryCategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.InMemoryProduitRepository;
import ci.komobe.actionelle.domain.repositories.InMemorySouscriptionRepository;
import ci.komobe.actionelle.domain.repositories.InMemoryVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.repositories.VehiculeRepository;
import ci.komobe.actionelle.domain.valueobjects.Valeur;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Moro KONÉ 2025-05-28
 */
@DisplayName("CreerSouscriptionUseCase")
class CreerSouscriptionUseCaseTest {

  private VehiculeRepository vehiculeRepository;
  private AssureRepository assureRepository;
  private SouscriptionRepository souscriptionRepository;
  private CreerSouscriptionUseCase useCase;

  @BeforeEach
  void setUp() {
    vehiculeRepository = new InMemoryVehiculeRepository();
    assureRepository = new InMemoryAssureRepository();
    souscriptionRepository = new InMemorySouscriptionRepository();
    var categorieVehiculeRepository = new InMemoryCategorieVehiculeRepository();
    var produitRepository = new InMemoryProduitRepository();

    useCase = new CreerSouscriptionUseCase(
        vehiculeRepository,
        assureRepository,
        souscriptionRepository,
        categorieVehiculeRepository,
        produitRepository);
  }

  @Nested
  @DisplayName("execute")
  class Execute {

    @Test
    @DisplayName("devrait créer une souscription avec succès")
    void devraitCreerUneSouscriptionAvecSucces() {
      // Given
      var dateMiseEnCirculation = LocalDate.now().minusYears(2);
      var dateNaissance = LocalDate.now().minusYears(30);

      var vehiculeCommand = CreerVehiculeCommand.builder()
          .immatriculation("ABC123")
          .couleur("Rouge")
          .nombreDeSieges(4)
          .nombreDePortes(5)
          .categorieCode("201")
          .dateMiseEnCirculation(dateMiseEnCirculation)
          .puissanceFiscale(10)
          .valeurNeuf(BigDecimal.valueOf(10_000_000))
          .build();

      var assureCommand = CreerAssureCommand.builder()
          .numeroCarteIdentite("CI123")
          .nom("Doe")
          .prenoms("John")
          .dateNaissance(dateNaissance)
          .lieuNaissance("Abidjan")
          .email("john.doe@example.com")
          .telephone("+2250123456789")
          .adresse("Abidjan")
          .build();

      BigDecimal valeurVenale = BigDecimal.valueOf(8_000_000);
      var command = CreerSouscriptionCommand.builder()
          .vehicule(vehiculeCommand)
          .assure(assureCommand)
          .vehiculeValeurVenale(valeurVenale)
          .produit("Papillon")
          .build();

      // When
      useCase.execute(command);

      // Then
      assertThat(souscriptionRepository.lister()).hasSize(1);

      Souscription souscription = souscriptionRepository.lister().getFirst();
      assertThat(souscription)
          .isNotNull()
          .satisfies(s -> {
            assertThat(s.getId()).isNotNull();
            assertThat(s.getNumero()).isNotNull();
            assertThat(s.getDateSouscription()).isEqualTo(LocalDate.now());
            assertThat(s.getVehiculeValeurVenale()).isEqualTo(Valeur.of(valeurVenale, "XOF"));

            assertThat(s.getVehicule())
                .isNotNull()
                .satisfies(v -> {
                  assertThat(v.getImmatriculation()).isEqualTo("ABC123");
                  assertThat(v.getCouleur()).isEqualTo("Rouge");
                  assertThat(v.getNombreDeSieges()).isEqualTo(4);
                  assertThat(v.getNombreDePortes()).isEqualTo(5);
                  assertThat(v.getCategorie().getCode()).isEqualTo("201");
                  assertThat(v.getPuissanceFiscale()).isEqualTo(10);
                  assertThat(v.getValeurNeuf().getMontant()).isEqualByComparingTo(
                      BigDecimal.valueOf(10_000_000));
                });

            assertThat(s.getAssure())
                .isNotNull()
                .satisfies(a -> {
                  assertThat(a.getNumeroCarteIdentite()).isEqualTo("CI123");
                  assertThat(a.getNom()).isEqualTo("Doe");
                  assertThat(a.getPrenoms()).isEqualTo("John");
                  assertThat(a.getDateNaissance()).isEqualTo(dateNaissance);
                  assertThat(a.getLieuNaissance()).isEqualTo("Abidjan");
                  assertThat(a.getEmail()).isEqualTo("john.doe@example.com");
                  assertThat(a.getTelephone()).isEqualTo("+2250123456789");
                  assertThat(a.getAdresse()).isEqualTo("Abidjan");
                });

            assertThat(s.getProduit())
                .isNotNull()
                .satisfies(p -> {
                  assertThat(p.getNom()).isEqualTo("Papillon");
                  assertThat(p.getGaranties()).isNotEmpty();
                });
          });
    }

    @Test
    @DisplayName("devrait échouer si le produit n'existe pas")
    void devraitEchouerSiProduitInexistant() {
      // Given
      var dateMiseEnCirculation = LocalDate.now().minusYears(2);
      var dateNaissance = LocalDate.now().minusYears(30);

      var vehiculeCommand = CreerVehiculeCommand.builder()
          .immatriculation("ABC123")
          .couleur("Rouge")
          .nombreDeSieges(4)
          .nombreDePortes(5)
          .categorieCode("201")
          .dateMiseEnCirculation(dateMiseEnCirculation)
          .puissanceFiscale(10)
          .valeurNeuf(BigDecimal.valueOf(10_000_000))
          .build();

      var assureCommand = CreerAssureCommand.builder()
          .numeroCarteIdentite("CI123")
          .nom("Doe")
          .prenoms("John")
          .dateNaissance(dateNaissance)
          .lieuNaissance("Abidjan")
          .email("john.doe@example.com")
          .telephone("+2250123456789")
          .adresse("Abidjan")
          .build();

      var command = CreerSouscriptionCommand.builder()
          .vehicule(vehiculeCommand)
          .assure(assureCommand)
          .vehiculeValeurVenale(BigDecimal.valueOf(8_000_000))
          .produit("ProduitInexistant")
          .build();

      // When & Then
      assertThatThrownBy(() -> useCase.execute(command))
          .isInstanceOf(ProduitErreur.class)
          .hasMessage("Le produit ProduitInexistant n'existe pas");

      assertThat(souscriptionRepository.lister()).isEmpty();
      assertThat(vehiculeRepository.lister()).isEmpty();
      assertThat(assureRepository.lister()).isEmpty();
    }

    @Test
    @DisplayName("devrait échouer si la valeur vénale est supérieure à la valeur à neuf")
    void devraitEchouerSiValeurVenaleSuperieure() {
      // Given
      var dateMiseEnCirculation = LocalDate.now().minusYears(2);
      var dateNaissance = LocalDate.now().minusYears(30);

      var vehiculeCommand = CreerVehiculeCommand.builder()
          .immatriculation("ABC123")
          .couleur("Rouge")
          .nombreDeSieges(4)
          .nombreDePortes(5)
          .categorieCode("201")
          .dateMiseEnCirculation(dateMiseEnCirculation)
          .puissanceFiscale(10)
          .valeurNeuf(BigDecimal.valueOf(10_000_000))
          .build();

      var assureCommand = CreerAssureCommand.builder()
          .numeroCarteIdentite("CI123")
          .nom("Doe")
          .prenoms("John")
          .dateNaissance(dateNaissance)
          .lieuNaissance("Abidjan")
          .email("john.doe@example.com")
          .telephone("+2250123456789")
          .adresse("Abidjan")
          .build();

      var command = CreerSouscriptionCommand.builder()
          .vehicule(vehiculeCommand)
          .assure(assureCommand)
          .vehiculeValeurVenale(BigDecimal.valueOf(12_000_000))
          .produit("Papillon")
          .build();

      // When & Then
      assertThatThrownBy(() -> useCase.execute(command))
          .isInstanceOf(RuntimeException.class);

      assertThat(souscriptionRepository.lister()).isEmpty();
      assertThat(vehiculeRepository.lister()).isEmpty();
      assertThat(assureRepository.lister()).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   " })
    @DisplayName("devrait échouer si le produit est invalide")
    void devraitEchouerSiProduitInvalide(String produitInvalide) {
      // Given
      var dateMiseEnCirculation = LocalDate.now().minusYears(2);
      var dateNaissance = LocalDate.now().minusYears(30);

      var vehiculeCommand = CreerVehiculeCommand.builder()
          .immatriculation("ABC123")
          .couleur("Rouge")
          .nombreDeSieges(4)
          .nombreDePortes(5)
          .categorieCode("201")
          .dateMiseEnCirculation(dateMiseEnCirculation)
          .puissanceFiscale(10)
          .valeurNeuf(BigDecimal.valueOf(10_000_000))
          .build();

      var assureCommand = CreerAssureCommand.builder()
          .numeroCarteIdentite("CI123")
          .nom("Doe")
          .prenoms("John")
          .dateNaissance(dateNaissance)
          .lieuNaissance("Abidjan")
          .email("john.doe@example.com")
          .telephone("+2250123456789")
          .adresse("Abidjan")
          .build();

      var command = CreerSouscriptionCommand.builder()
          .vehicule(vehiculeCommand)
          .assure(assureCommand)
          .vehiculeValeurVenale(BigDecimal.valueOf(8_000_000))
          .produit(produitInvalide)
          .build();

      // When & Then
      assertThatThrownBy(() -> useCase.execute(command))
          .isInstanceOf(ProduitErreur.class)
          .hasMessage("Le produit " + produitInvalide + " n'existe pas");

      assertThat(souscriptionRepository.lister()).isEmpty();
      assertThat(vehiculeRepository.lister()).isEmpty();
      assertThat(assureRepository.lister()).isEmpty();
    }
  }
}
