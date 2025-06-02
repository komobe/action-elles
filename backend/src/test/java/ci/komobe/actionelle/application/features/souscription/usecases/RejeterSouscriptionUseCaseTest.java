package ci.komobe.actionelle.application.features.souscription.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.exceptions.SouscriptionErreur;
import ci.komobe.actionelle.domain.repositories.InMemorySouscriptionRepository;
import ci.komobe.actionelle.domain.valueobjects.StatutSouscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RejeterSouscriptionUseCase")
class RejeterSouscriptionUseCaseTest {

  private InMemorySouscriptionRepository souscriptionRepository;
  private RejeterSouscriptionUseCase useCase;

  @BeforeEach
  void setUp() {
    souscriptionRepository = new InMemorySouscriptionRepository();
    useCase = new RejeterSouscriptionUseCase(souscriptionRepository);
  }

  @Test
  @DisplayName("devrait rejeter une souscription avec succès")
  void devraitRejeterUneSouscriptionAvecSucces() {
    // Given
    var souscription = new Souscription();
    souscription.setId("123");
    souscription.setStatut(StatutSouscription.EN_ATTENTE_VALIDATION);
    souscriptionRepository.enregistrer(souscription);

    // When
    useCase.execute("123");

    // Then
    var souscriptionRejetee = souscriptionRepository.chercherParId("123").orElseThrow();
    assertThat(souscriptionRejetee.getStatut()).isEqualTo(StatutSouscription.REJETEE);
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