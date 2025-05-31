package ci.komobe.actionelle.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ci.komobe.actionelle.application.features.devis.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.application.features.vehicule.CategorieVehiculeError;
import ci.komobe.actionelle.application.features.produit.ProduitError;
import ci.komobe.actionelle.application.features.devis.usecases.SimulerPrimeUseCase;
import ci.komobe.actionelle.application.features.devis.presenters.DefaultSimulerPrimePresenter;
import ci.komobe.actionelle.application.features.devis.presenters.SimulerPrimePresenter;
import ci.komobe.actionelle.application.repositories.InMemoryProduitRepository;
import ci.komobe.actionelle.application.commons.services.prime.PrimeCalculator;
import ci.komobe.actionelle.application.commons.services.prime.PrimeMontantFixeStrategy;
import ci.komobe.actionelle.application.commons.services.prime.PrimePourcentageStrategy;
import ci.komobe.actionelle.application.features.devis.dto.SimulationPrimeResult;
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
  private SimulerPrimePresenter<SimulationPrimeResult> presenter;

  @BeforeEach
  void setUp() {
    var produitRepository = new InMemoryProduitRepository();
    var primeCalculator = new PrimeCalculator();
    primeCalculator.addStrategy(TypeMontantPrime.POURCENTAGE, new PrimePourcentageStrategy());
    primeCalculator.addStrategy(TypeMontantPrime.MONTANT, new PrimeMontantFixeStrategy());
    useCase = new SimulerPrimeUseCase(produitRepository, primeCalculator);
    presenter = new DefaultSimulerPrimePresenter();
  }

  @Test
  @DisplayName("Simuler le calcul de la prime d'un produit Papillon avec une puissance fiscale de 5")
  void simulerCalculPrimeProduitPapillonAvecPuissanceFiscale5() {
    var command = new SimulerPrimeCommand();
    command.setProduit("Papillon");
    command.setCategorie("201");
    command.setPuissanceFiscale(5);
    command.setDateDeMiseEnCirculation(LocalDate.of(2022, 1, 1));
    command.setValeurNeuf(BigDecimal.valueOf(10_000_000));
    command.setValeurVenale(BigDecimal.valueOf(6_000_000));

    useCase.execute(command, presenter);

    SimulationPrimeResult simulationPrimeResult = presenter.present();
    assertEquals(0, BigDecimal.valueOf(313_581).compareTo(simulationPrimeResult.price()));
  }

  @Test
  @DisplayName("Simuler le calcul de la prime d'un produit Douby avec une puissance fiscale de 5")
  void simulerCalculPrimeProduitDoubyAvecPuissanceFiscale5() {
    var command = new SimulerPrimeCommand();
    command.setProduit("Douby");
    command.setCategorie("202");
    command.setPuissanceFiscale(5);
    command.setDateDeMiseEnCirculation(LocalDate.of(2020, 6, 1));
    command.setValeurNeuf(BigDecimal.valueOf(10_000_000));
    command.setValeurVenale(BigDecimal.valueOf(6_500_000));

    useCase.execute(command, presenter);

    SimulationPrimeResult simulationPrimeResult = presenter.present();
    assertEquals(0, BigDecimal.valueOf(470_181).compareTo(simulationPrimeResult.price()));
  }

  @Test
  @DisplayName("Simuler le calcul de la prime d'un produit Douyou avec une puissance fiscale de 5")
  void simulerCalculPrimeProduitDouyouAvecPuissanceFiscale5() {
    var command = new SimulerPrimeCommand();
    command.setProduit("Douyou");
    command.setCategorie("202");
    command.setPuissanceFiscale(5);
    command.setDateDeMiseEnCirculation(LocalDate.of(2021, 5, 1));
    command.setValeurNeuf(BigDecimal.valueOf(8_000_000));
    command.setValeurVenale(BigDecimal.valueOf(5_000_000));

    useCase.execute(command, presenter);
    var simulationPrimeResult = presenter.present();
    assertEquals(0, BigDecimal.valueOf(392_681).compareTo(simulationPrimeResult.price()));
  }

  @Test
  @DisplayName("Simuler le calcul de la prime d'un produit Toutourisquou avec une puissance fiscale de 5")
  void simulerCalculPrimeProduitToutourisquouAvecPuissanceFiscale5() {
    var command = new SimulerPrimeCommand();
    command.setProduit("Toutourisquou");
    command.setCategorie("201");
    command.setPuissanceFiscale(5);
    command.setDateDeMiseEnCirculation(LocalDate.of(2020, 3, 1));
    command.setValeurNeuf(BigDecimal.valueOf(12_000_000));
    command.setValeurVenale(BigDecimal.valueOf(7_000_000));
    command.setValeurVenale(BigDecimal.valueOf(7_000_000));

    useCase.execute(command, presenter);

    var simulationPrimeResult = presenter.present();

    assertEquals(0, BigDecimal.valueOf(410_481).compareTo(simulationPrimeResult.price()));
  }

  @Test
  @DisplayName("Echec de la simulation de la prime car le produit n'existe pas")
  void echecDeLaSimulationDeLaPrimeCarLeProduitNexistePas() {
    var command = new SimulerPrimeCommand();
    command.setProduit("Papillon2");
    command.setCategorie("201");
    command.setPuissanceFiscale(10);
    command.setDateDeMiseEnCirculation(LocalDate.of(2021, 1, 1));
    command.setValeurNeuf(BigDecimal.valueOf(10_000_000));
    command.setValeurVenale(BigDecimal.valueOf(6_000_000));

    Assertions.assertThrows(ProduitError.class, () -> useCase.execute(command, presenter));
  }

  @Test
  @DisplayName("Echec de la simulation de la prime car le produit n'a pas de catégorie de véhicule")
  void echecDeLaSimulationDeLaPrimeCarLeProduitNaCategorieVehicule() {
    var command = new SimulerPrimeCommand();
    command.setProduit("Papillon");
    command.setCategorie("100");
    command.setPuissanceFiscale(11);
    command.setDateDeMiseEnCirculation(LocalDate.of(2021, 1, 1));
    command.setValeurNeuf(BigDecimal.valueOf(10_000_000));
    command.setValeurVenale(BigDecimal.valueOf(6_000_000));

    Assertions.assertThrows(CategorieVehiculeError.class, () -> useCase.execute(command, presenter));
  }
}
