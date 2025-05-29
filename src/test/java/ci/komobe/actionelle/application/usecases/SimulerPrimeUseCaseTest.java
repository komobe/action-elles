package ci.komobe.actionelle.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ci.komobe.actionelle.application.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.application.exceptions.ProduitError;
import ci.komobe.actionelle.application.presenters.SimulerPrimePresenter;
import ci.komobe.actionelle.application.repositories.InMemoryProduitRepository;
import ci.komobe.actionelle.application.services.prime.PrimeCalculator;
import ci.komobe.actionelle.application.services.prime.PrimeMontantFixeStrategy;
import ci.komobe.actionelle.application.services.prime.PrimePourcentageStrategy;
import ci.komobe.actionelle.domain.valueobjects.TypeMontantPrime;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires pour la simulation du calcul de prime.
 *
 * @author Moro KONÉ 2025-05-29
 */
class SimulerPrimeUseCaseTest {

  private SimulerPrimeUseCase useCase;
  private SimulerPrimePresenter presenter;

  @BeforeEach
  void setUp() {
    var produitRepository = new InMemoryProduitRepository();
    var primeCalculator = new PrimeCalculator();
    primeCalculator.addStrategy(TypeMontantPrime.POURCENTAGE, new PrimePourcentageStrategy());
    primeCalculator.addStrategy(TypeMontantPrime.MONTANT, new PrimeMontantFixeStrategy());
    useCase = new SimulerPrimeUseCase(produitRepository, primeCalculator);
    presenter = new SimulerPrimePresenter() {
      private Object data;

      @Override
      public void addData(Object data) {
        this.data = data;
      }

      @Override
      public Object present() {
        return data;
      }
    };
  }

  @Test
  @DisplayName("Simuler le calcul de la prime d'un produit Papillon avec une puissance fiscale de 5")
  void simulerCalculPrimeProduitPapillonAvecPuissanceFiscale5() {
    var command = new SimulerPrimeCommand(
        "Papillon",
        "201",
        5,
        LocalDate.of(2022, 1, 1),
        BigDecimal.valueOf(10_000_000),
        BigDecimal.valueOf(6_000_000));

    useCase.execute(command, presenter);

    assertEquals(0, BigDecimal.valueOf(313_581).compareTo((BigDecimal) presenter.present()));
  }

  @Test
  @DisplayName("Simuler le calcul de la prime d'un produit Douby avec une puissance fiscale de 5")
  void simulerCalculPrimeProduitDoubyAvecPuissanceFiscale5() {
    var command = new SimulerPrimeCommand(
        "Douby",
        "202",
        5,
        LocalDate.of(2020, 6, 1),
        BigDecimal.valueOf(10_000_000),
        BigDecimal.valueOf(6_500_000));

    useCase.execute(command, presenter);

    assertEquals(0, BigDecimal.valueOf(470_181).compareTo((BigDecimal) presenter.present()));
  }

  @Test
  @DisplayName("Simuler le calcul de la prime d'un produit Douyou avec une puissance fiscale de 5")
  void simulerCalculPrimeProduitDouyouAvecPuissanceFiscale5() {
    var command = new SimulerPrimeCommand(
        "Douyou",
        "202",
        5,
        LocalDate.of(2021, 5, 1),
        BigDecimal.valueOf(8_000_000),
        BigDecimal.valueOf(5_000_000));

    useCase.execute(command, presenter);

    assertEquals(0, BigDecimal.valueOf(392_681).compareTo((BigDecimal) presenter.present()));
  }

  @Test
  @DisplayName("Simuler le calcul de la prime d'un produit Toutourisquou avec une puissance fiscale de 5")
  void simulerCalculPrimeProduitToutourisquouAvecPuissanceFiscale5() {
    var command = new SimulerPrimeCommand(
        "Toutourisquou",
        "201",
        5,
        LocalDate.of(2020, 3, 1),
        BigDecimal.valueOf(12_000_000),
        BigDecimal.valueOf(7_000_000));

    useCase.execute(command, presenter);

    assertEquals(0, BigDecimal.valueOf(410_481).compareTo((BigDecimal) presenter.present()));
  }

  @Test
  @DisplayName("Echec de la simulation de la prime car le produit n'existe pas")
  void echecDeLaSimulationDeLaPrimeCarLeProduitNexistePas() {
    var command = new SimulerPrimeCommand(
        "Papillon2",
        "201",
        10,
        LocalDate.of(2021, 1, 1),
        BigDecimal.valueOf(10_000_000),
        BigDecimal.valueOf(6_000_000));

    Assertions.assertThrows(ProduitError.class, () -> useCase.execute(command, presenter));
  }

  @Test
  @DisplayName("Echec de la simulation de la prime car le produit n'a pas de catégorie de véhicule")
  void echecDeLaSimulationDeLaPrimeCarLeProduitNaCategorieVehicule() {
    var command = new SimulerPrimeCommand(
        "Papillon",
        "100",
        11,
        LocalDate.of(2021, 1, 1),
        BigDecimal.valueOf(10_000_000),
        BigDecimal.valueOf(6_000_000));

    Assertions.assertThrows(ProduitError.class, () -> useCase.execute(command, presenter));
  }
}
