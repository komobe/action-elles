package ci.komobe.actionelle.application.features.produit.usecases;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ci.komobe.actionelle.application.features.produit.commands.CreerProduitCommand;
import ci.komobe.actionelle.domain.exceptions.ProduitErreur;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreerProduitUseCaseTest {

  @Mock
  private ProduitRepository produitRepository;

  private CreerProduitUseCase useCase;

  @BeforeEach
  void setUp() {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    useCase = new CreerProduitUseCase(produitRepository, validator);
  }

  @Test
  @DisplayName("Test que la création d'un produit échoue car code existe déjà")
  void creationProduitEchoueCarCodeExiste() {
    // Given
    var code = "RC1234";
    var command = CreerProduitCommand.builder()
        .code(code)
        .libelle("Responsabilité Civile Auto")
        .description("Assurance responsabilité civile automobile")
        .tauxCommission(new BigDecimal("10.0"))
        .tauxPrime(new BigDecimal("5.0"))
        .build();

    when(produitRepository.existeParCode(code)).thenReturn(true);

    // When & Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(ProduitErreur.class)
        .hasMessage("Un produit avec le code " + code + " existe déjà");

    verify(produitRepository, never()).enregistrer(any());
  }

  @Test
  @DisplayName("Test que la création d'un produit échoue car validation échoue")
  void creationProduitEchoueCarValidationEchoue() {
    // Given
    var command = CreerProduitCommand.builder()
        .code("invalid")
        .libelle("")
        .tauxCommission(new BigDecimal("-1.0"))
        .tauxPrime(new BigDecimal("101.0"))
        .build();

    // When & Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(ProduitErreur.class)
        .hasMessageContaining("Validation échouée");

    verify(produitRepository, never()).enregistrer(any());
  }

  @Test
  @DisplayName("Test que la création d'un produit se fait avec succès")
  void creationProduitSeFaitAvecSucces() {
    // Given
    var command = CreerProduitCommand.builder()
        .code("RC1234")
        .libelle("Responsabilité Civile Auto")
        .description("Assurance responsabilité civile automobile")
        .tauxCommission(new BigDecimal("10.0"))
        .tauxPrime(new BigDecimal("5.0"))
        .build();

    when(produitRepository.existeParCode(command.getCode())).thenReturn(false);

    // When
    useCase.execute(command);

    // Then
    verify(produitRepository).enregistrer(any());
  }
}