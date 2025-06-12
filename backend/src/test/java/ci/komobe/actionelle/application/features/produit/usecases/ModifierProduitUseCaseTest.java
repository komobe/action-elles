package ci.komobe.actionelle.application.features.produit.usecases;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ci.komobe.actionelle.application.features.produit.commands.ModifierProduitCommand;
import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.exceptions.ProduitErreur;
import ci.komobe.actionelle.domain.repositories.CategorieVehiculeRepository;
import ci.komobe.actionelle.domain.repositories.GarantieRepository;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ModifierProduitUseCaseTest {

  @Mock
  private ProduitRepository produitRepository;

  @Mock
  private GarantieRepository garantieRepository;

  @Mock
  private CategorieVehiculeRepository categorieVehiculeRepository;

  private ModifierProduitUseCase useCase;

  @BeforeEach
  void setUp() {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    useCase = new ModifierProduitUseCase(produitRepository, garantieRepository, categorieVehiculeRepository, validator);
  }

  @Test
  @DisplayName("Test que la modification d'un produit échoue car produit non trouvé")
  void modificationProduitEchoueCarProduitNonTrouve() {
    // Given
    var produitId = IdGenerator.generateId();
    var command = ModifierProduitCommand.builder()
        .id(produitId)
        .code("RC1234")
        .libelle("Responsabilité Civile Auto")
        .description("Assurance responsabilité civile automobile")
        .tauxCommission(new BigDecimal("10.0"))
        .tauxPrime(new BigDecimal("5.0"))
        .garanties(List.of("RC", "DOMMAGE"))
        .categoriesVehicules(List.of("201", "202"))
        .build();

    when(produitRepository.chercherParId(command.getId())).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(ProduitErreur.class)
        .hasMessage("Produit non trouvé");

    verify(produitRepository, never()).enregistrer(any());
  }

  @Test
  @DisplayName("Test que la modification d'un produit échoue car code existe déjà")
  void modificationProduitEchoueCarCodeExiste() {
    // Given
    var produitId = IdGenerator.generateId();
    var command = ModifierProduitCommand.builder()
        .id(produitId)
        .code("RC1234")
        .libelle("Responsabilité Civile Auto")
        .description("Assurance responsabilité civile automobile")
        .tauxCommission(new BigDecimal("10.0"))
        .tauxPrime(new BigDecimal("5.0"))
        .garanties(List.of("RC", "DOMMAGE"))
        .categoriesVehicules(List.of("201", "202"))
        .build();

    var produit = Produit.builder()
        .id(produitId)
        .code("RC5678")
        .nom("Responsabilité Civile Auto")
        .description("Assurance responsabilité civile automobile")
        .build();

    when(produitRepository.chercherParId(command.getId())).thenReturn(Optional.of(produit));
    when(produitRepository.existeParCode(command.getCode())).thenReturn(true);

    // When & Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(ProduitErreur.class)
        .hasMessage("Un produit avec le code " + command.getCode() + " existe déjà");

    verify(produitRepository, never()).enregistrer(any());
  }

  @Test
  @DisplayName("Test que la modification d'un produit échoue car validation échoue")
  void modificationProduitEchoueCarValidationEchoue() {
    // Given
    var produitId = IdGenerator.generateId();
    var command = ModifierProduitCommand.builder()
        .id(produitId)
        .code("invalid")
        .libelle("")
        .tauxCommission(new BigDecimal("-1.0"))
        .tauxPrime(new BigDecimal("101.0"))
        .garanties(List.of("RC", "DOMMAGE"))
        .categoriesVehicules(List.of("201", "202"))
        .build();

    // When & Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(ProduitErreur.class)
        .hasMessageContaining("Validation échouée");

    verify(produitRepository, never()).enregistrer(any());
  }

  @Test
  @DisplayName("Test que la modification d'un produit se fait avec succès")
  void modificationProduitSeFaitAvecSucces() {
    // Given
    var produitId = IdGenerator.generateId();
    var command = ModifierProduitCommand.builder()
        .id(produitId)
        .code("RC1234")
        .libelle("Responsabilité Civile Auto")
        .description("Assurance responsabilité civile automobile")
        .tauxCommission(new BigDecimal("10.0"))
        .tauxPrime(new BigDecimal("5.0"))
        .garanties(List.of("RC", "DOMMAGE"))
        .categoriesVehicules(List.of("201", "202"))
        .build();

    var produit = Produit.builder()
        .id(produitId)
        .code("RC5678")
        .nom("Ancien nom")
        .description("Ancienne description")
        .build();

    when(produitRepository.chercherParId(produitId)).thenReturn(Optional.of(produit));
    when(produitRepository.existeParCode("RC1234")).thenReturn(false);

    // Mock des garanties
    var garantieRC = Garantie.builder().id(IdGenerator.generateId()).code("RC").build();
    var garantieDommage = Garantie.builder().id(IdGenerator.generateId()).code("DOMMAGE").build();
    when(garantieRepository.chercherParCode("RC")).thenReturn(Optional.of(garantieRC));
    when(garantieRepository.chercherParCode("DOMMAGE")).thenReturn(Optional.of(garantieDommage));

    // Mock des catégories
    var categorie201 = CategorieVehicule.builder().id(IdGenerator.generateId()).code("201").build();
    var categorie202 = CategorieVehicule.builder().id(IdGenerator.generateId()).code("202").build();
    when(categorieVehiculeRepository.chercherParCode("201")).thenReturn(Optional.of(categorie201));
    when(categorieVehiculeRepository.chercherParCode("202")).thenReturn(Optional.of(categorie202));

    // When
    useCase.execute(command);

    // Then
    verify(produitRepository).enregistrer(any());
  }
}