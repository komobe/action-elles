package ci.komobe.actionelle.application.features.souscription.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.exceptions.SouscriptionErreur;
import ci.komobe.actionelle.domain.repositories.InMemorySouscriptionRepository;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.valueobjects.StatutSouscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ValiderSouscriptionUseCase")
class ValiderSouscriptionUseCaseTest {

  private SouscriptionRepository souscriptionRepository;
  private ValiderSouscriptionUseCase useCase;

  @BeforeEach
  void setUp() {
    souscriptionRepository = new InMemorySouscriptionRepository();
    useCase = new ValiderSouscriptionUseCase(souscriptionRepository);
  }

  @Test
  @DisplayName("devrait valider une souscription avec succès")
  void devraitValiderUneSouscriptionAvecSucces() {
    // Given
    var souscription = new Souscription();
    souscription.setId("123");
    souscription.setStatut(StatutSouscription.EN_ATTENTE_VALIDATION);
    souscriptionRepository.enregistrer(souscription);

    // When
    useCase.execute("123");

    // Then
    var souscriptionValidee = souscriptionRepository.chercherParId("123").orElseThrow();
    assertThat(souscriptionValidee.getStatut()).isEqualTo(StatutSouscription.VALIDEE);
  }

  @Test
  @DisplayName("devrait échouer si la souscription n'existe pas")
  void devraitEchouerSiSouscriptionInexistante() {
    // Then
    assertThatThrownBy(() -> useCase.execute("123"))
        .isInstanceOf(SouscriptionErreur.class)
        .hasMessage("La souscription 123 n'existe pas");
  }
}