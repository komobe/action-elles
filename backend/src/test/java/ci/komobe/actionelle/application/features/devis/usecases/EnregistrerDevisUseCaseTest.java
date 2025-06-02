package ci.komobe.actionelle.application.features.devis.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ci.komobe.actionelle.application.commons.services.prime.PrimeCalculator;
import ci.komobe.actionelle.application.features.devis.commands.EnregistrerDevisCommand;
import ci.komobe.actionelle.domain.entities.Devis;
import ci.komobe.actionelle.domain.exceptions.DevisErreur;
import ci.komobe.actionelle.domain.repositories.DevisRepository;
import ci.komobe.actionelle.domain.repositories.InMemoryDevisRepository;
import ci.komobe.actionelle.domain.repositories.InMemoryProduitRepository;
import ci.komobe.actionelle.domain.repositories.InMemoryVehiculeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnregistrerDevisUseCase")
class EnregistrerDevisUseCaseTest {

  private DevisRepository devisRepository;

  @Mock
  private PrimeCalculator primeCalculator;

  private EnregistrerDevisUseCase useCase;

  @BeforeEach
  void setUp() {
    devisRepository = new InMemoryDevisRepository();
    var produitRepository = new InMemoryProduitRepository();
    var vehiculeRepository = new InMemoryVehiculeRepository();

    useCase = new EnregistrerDevisUseCase(
        produitRepository,
        primeCalculator,
        devisRepository,
        vehiculeRepository);
  }

  @Test
  @DisplayName("devrait enregistrer un devis avec succès")
  void devraitEnregistrerUnDevisAvecSucces() {
    // Given
    var dateMiseEnCirculation = LocalDate.of(2020, 1, 1);
    var command = new EnregistrerDevisCommand(
        "QUOTE-123",
        dateMiseEnCirculation,
        BigDecimal.valueOf(50_000),
        "ABC123");
    command.setProduit("Papillon");
    command.setPuissanceFiscale(10);
    command.setValeurNeuf(BigDecimal.valueOf(10_000_000));
    command.setValeurVenale(BigDecimal.valueOf(8_000_000));
    command.setCategorie("201");

    when(primeCalculator.calculPrime(any(), any())).thenReturn(BigDecimal.valueOf(50_000));

    // When
    useCase.execute(command);

    // Then
    assertThat(devisRepository.lister()).hasSize(1);
    var devis = devisRepository.lister().getFirst();
    assertThat(devis.getReference()).isEqualTo("QUOTE-123");
    assertThat(devis.getMontantPrime()).isEqualByComparingTo(BigDecimal.valueOf(50_000));
  }

  @Test
  @DisplayName("devrait échouer si le devis existe déjà")
  void devraitEchouerSiDevisExisteDeja() {
    // Given
    var dateMiseEnCirculation = LocalDate.of(2020, 1, 1);
    var command = new EnregistrerDevisCommand(
        "QUOTE-123",
        dateMiseEnCirculation,
        BigDecimal.valueOf(50_000),
        "ABC123");
    command.setProduit("Papillon");
    command.setPuissanceFiscale(10);
    command.setValeurNeuf(BigDecimal.valueOf(10_000_000));
    command.setValeurVenale(BigDecimal.valueOf(8_000_000));
    command.setCategorie("201");

    // Enregistrer un devis avec la même référence
    var premierDevis = Devis.builder()
        .reference("QUOTE-123")
        .montantPrime(BigDecimal.valueOf(50_000))
        .build();
    devisRepository.enregistrer(premierDevis);

    // Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(DevisErreur.class)
        .hasMessage("Le devis existe déjà");
  }
}