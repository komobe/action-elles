package ci.komobe.actionelle.application.features.produit.usecases;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.exceptions.ProduitErreur;
import ci.komobe.actionelle.domain.repositories.ProduitRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SupprimerProduitUseCaseTest {

  @Mock
  private ProduitRepository produitRepository;

  private SupprimerProduitUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new SupprimerProduitUseCase(produitRepository);
  }

  @Test
  @DisplayName("Test que la suppression d'un produit échoue car produit non trouvé")
  void suppressionProduitEchoueCarProduitNonTrouve() {
    // Given
    String id = "PROD-001";
    when(produitRepository.chercherParId(id)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> useCase.execute(id))
        .isInstanceOf(ProduitErreur.class)
        .hasMessage("Produit non trouvé");

    verify(produitRepository, never()).supprimer(any(Produit.class));
  }

  @Test
  @DisplayName("Test que la suppression d'un produit se fait avec succès")
  void suppressionProduitSeFaitAvecSucces() {
    // Given
    String id = "PROD-001";
    var produit = Produit.builder()
        .id(id)
        .code("RC1234")
        .nom("Assurance Auto")
        .description("Assurance automobile tous risques")
        .build();

    when(produitRepository.chercherParId(id)).thenReturn(Optional.of(produit));

    // When
    useCase.execute(id);

    // Then
    verify(produitRepository).supprimer(produit);
  }
}