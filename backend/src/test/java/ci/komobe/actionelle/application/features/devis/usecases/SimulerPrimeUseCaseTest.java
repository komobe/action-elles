package ci.komobe.actionelle.application.features.devis.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ci.komobe.actionelle.application.commons.services.prime.PrimeCalculator;
import ci.komobe.actionelle.application.features.devis.commands.SimulerPrimeCommand;
import ci.komobe.actionelle.application.features.devis.presenters.DefaultSimulerPrimePresenter;
import ci.komobe.actionelle.domain.exceptions.ProduitErreur;
import ci.komobe.actionelle.domain.repositories.InMemoryProduitRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests unitaires pour la simulation du calcul de prime.
 *
 * @author Moro KONÉ 2025-05-29
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SimulerPrimeUseCase")
class SimulerPrimeUseCaseTest {

  @Mock
  private PrimeCalculator primeCalculator;

  private SimulerPrimeUseCase useCase;

  @BeforeEach
  void setUp() {
    var produitRepository = new InMemoryProduitRepository();
    useCase = new SimulerPrimeUseCase(produitRepository, primeCalculator);
  }

  @Test
  @DisplayName("devrait simuler la prime avec succès")
  void devraitSimulerLaPrimeAvecSucces() {
    // Given
    var command = new SimulerPrimeCommand();
    command.setProduit("Papillon");
    command.setPuissanceFiscale(10);
    command.setValeurNeuf(BigDecimal.valueOf(10_000_000));
    command.setValeurVenale(BigDecimal.valueOf(8_000_000));
    command.setDateDeMiseEnCirculation(LocalDate.of(2020, 1, 1));
    command.setCategorie("201");

    when(primeCalculator.calculPrime(any(), any())).thenReturn(BigDecimal.valueOf(50_000));

    var presenter = new DefaultSimulerPrimePresenter();

    // When
    useCase.execute(command, presenter);
    var result = presenter.present();

    // Then
    assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(50_000));
  }

  @Test
  @DisplayName("devrait échouer si le produit n'existe pas")
  void devraitEchouerSiProduitInexistant() {
    // Given
    var command = new SimulerPrimeCommand();
    command.setProduit("ProduitInexistant");
    command.setPuissanceFiscale(10);
    command.setValeurNeuf(BigDecimal.valueOf(10_000_000));
    command.setValeurVenale(BigDecimal.valueOf(8_000_000));
    command.setDateDeMiseEnCirculation(LocalDate.of(2020, 1, 1));
    command.setCategorie("201");

    var presenter = new DefaultSimulerPrimePresenter();

    // Then
    assertThatThrownBy(() -> useCase.execute(command, presenter))
        .isInstanceOf(ProduitErreur.class)
        .hasMessage("Produit non trouvé");
  }
}
