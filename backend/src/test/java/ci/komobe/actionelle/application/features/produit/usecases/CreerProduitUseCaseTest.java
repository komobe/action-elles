package ci.komobe.actionelle.application.features.produit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ci.komobe.actionelle.application.features.produit.commands.CreerProduitCommand;
import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.entities.Garantie;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreerProduitUseCaseTest {

  @Mock
  private ProduitRepository produitRepository;

  @Mock
  private GarantieRepository garantieRepository;

  @Mock
  private CategorieVehiculeRepository categorieVehiculeRepository;

  private CreerProduitUseCase useCase;

  @BeforeEach
  void setUp() {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    useCase = new CreerProduitUseCase(produitRepository, garantieRepository, categorieVehiculeRepository, validator);
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
        .garanties(List.of("RC", "DOMMAGE"))
        .categoriesVehicules(List.of("201", "202"))
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
        .garanties(List.of("RC"))
        .categoriesVehicules(List.of("201"))
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
        .garanties(List.of("RC", "DOMMAGE"))
        .categoriesVehicules(List.of("201", "202"))
        .build();

    when(produitRepository.existeParCode(command.getCode())).thenReturn(false);

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

  @Test
  @DisplayName("Test que la création d'un produit enregistre correctement les catégories de véhicules")
  void creationProduitEnregistreCorrectementLesCategoriesVehicules() {
    // Given
    var command = CreerProduitCommand.builder()
        .code("RC1234")
        .libelle("Responsabilité Civile Auto")
        .description("Assurance responsabilité civile automobile")
        .tauxCommission(new BigDecimal("10.0"))
        .tauxPrime(new BigDecimal("5.0"))
        .garanties(List.of("RC"))
        .categoriesVehicules(List.of("201", "202", "203"))
        .build();

    when(produitRepository.existeParCode(command.getCode())).thenReturn(false);

    // Mock des garanties
    var garantieRC = Garantie.builder().id(IdGenerator.generateId()).code("RC").build();
    when(garantieRepository.chercherParCode("RC")).thenReturn(Optional.of(garantieRC));

    // Mock des catégories
    var categorie201 = CategorieVehicule.builder().id(IdGenerator.generateId()).code("201")
        .libelle("Promenade et Affaire").build();
    var categorie202 = CategorieVehicule.builder().id(IdGenerator.generateId()).code("202")
        .libelle("Véhicules Motorisés à 2 ou 3 roues")
        .build();
    var categorie203 = CategorieVehicule.builder().id(IdGenerator.generateId()).code("203")
        .libelle("Transport public de voyage").build();
    when(categorieVehiculeRepository.chercherParCode("201")).thenReturn(Optional.of(categorie201));
    when(categorieVehiculeRepository.chercherParCode("202")).thenReturn(Optional.of(categorie202));
    when(categorieVehiculeRepository.chercherParCode("203")).thenReturn(Optional.of(categorie203));

    // When
    useCase.execute(command);

    // Then
    ArgumentCaptor<ci.komobe.actionelle.domain.entities.Produit> produitCaptor = ArgumentCaptor
        .forClass(ci.komobe.actionelle.domain.entities.Produit.class);
    verify(produitRepository).enregistrer(produitCaptor.capture());

    var produitEnregistre = produitCaptor.getValue();
    assertThat(produitEnregistre.getCategoriesVehicules()).hasSize(3);
    assertThat(produitEnregistre.getCategoriesVehicules()).extracting(CategorieVehicule::getCode)
        .containsExactlyInAnyOrder("201", "202", "203");
    assertThat(produitEnregistre.getGaranties()).hasSize(1);
    assertThat(produitEnregistre.getGaranties()).extracting(Garantie::getCode)
        .containsExactly("RC");
  }

  @Test
  @DisplayName("Test que la création d'un produit échoue si une garantie n'existe pas")
  void creationProduitEchoueSiGarantieInexistante() {
    // Given
    var command = CreerProduitCommand.builder()
        .code("RC1234")
        .libelle("Responsabilité Civile Auto")
        .description("Assurance responsabilité civile automobile")
        .tauxCommission(new BigDecimal("10.0"))
        .tauxPrime(new BigDecimal("5.0"))
        .garanties(List.of("RC", "GARANTIE_INEXISTANTE"))
        .categoriesVehicules(List.of("201"))
        .build();

    when(produitRepository.existeParCode(command.getCode())).thenReturn(false);

    // Mock des garanties
    var garantieRC = Garantie.builder().id(IdGenerator.generateId()).code("RC").build();
    when(garantieRepository.chercherParCode("RC")).thenReturn(Optional.of(garantieRC));
    when(garantieRepository.chercherParCode("GARANTIE_INEXISTANTE")).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(ProduitErreur.class)
        .hasMessage("Garantie avec le code GARANTIE_INEXISTANTE non trouvée");

    verify(produitRepository, never()).enregistrer(any());
  }

  @Test
  @DisplayName("Test que la création d'un produit échoue si une catégorie de véhicule n'existe pas")
  void creationProduitEchoueSiCategorieVehiculeInexistante() {
    // Given
    var command = CreerProduitCommand.builder()
        .code("RC1234")
        .libelle("Responsabilité Civile Auto")
        .description("Assurance responsabilité civile automobile")
        .tauxCommission(new BigDecimal("10.0"))
        .tauxPrime(new BigDecimal("5.0"))
        .garanties(List.of("RC"))
        .categoriesVehicules(List.of("201", "CATEGORIE_INEXISTANTE"))
        .build();

    when(produitRepository.existeParCode(command.getCode())).thenReturn(false);

    // Mock des garanties
    var garantieRC = Garantie.builder().id(IdGenerator.generateId()).code("RC").build();
    when(garantieRepository.chercherParCode("RC")).thenReturn(Optional.of(garantieRC));

    // Mock des catégories
    var categorie201 = CategorieVehicule.builder().id(IdGenerator.generateId()).code("201").build();
    when(categorieVehiculeRepository.chercherParCode("201")).thenReturn(Optional.of(categorie201));
    when(categorieVehiculeRepository.chercherParCode("CATEGORIE_INEXISTANTE")).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> useCase.execute(command))
        .isInstanceOf(ProduitErreur.class)
        .hasMessage("Catégorie de véhicule avec le code CATEGORIE_INEXISTANTE non trouvée");

    verify(produitRepository, never()).enregistrer(any());
  }
}