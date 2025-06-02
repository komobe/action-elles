package ci.komobe.actionelle.application.features.utilisateur.usecases;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.exceptions.UtilisateurErreur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("SupprimerUtilisateurUseCase")
class SupprimerUtilisateurUseCaseTest {

  @Mock
  private UtilisateurRepository utilisateurRepository;

  @InjectMocks
  private SupprimerUtilisateurUseCase useCase;

  @Test
  @DisplayName("devrait supprimer un utilisateur avec succès")
  void devraitSupprimerUnUtilisateurAvecSucces() {
    // Given
    var utilisateurId = "123";
    var utilisateur = new Utilisateur();
    utilisateur.setId(utilisateurId);

    when(utilisateurRepository.chercherParId(utilisateurId))
        .thenReturn(Optional.of(utilisateur));

    // When
    useCase.execute(utilisateurId);

    // Then
    verify(utilisateurRepository).supprimer(utilisateur);
  }

  @Test
  @DisplayName("devrait échouer si l'utilisateur n'existe pas")
  void devraitEchouerSiUtilisateurInexistant() {
    // Given
    var utilisateurId = "123";

    when(utilisateurRepository.chercherParId(utilisateurId))
        .thenReturn(Optional.empty());

    // Then
    assertThatThrownBy(() -> useCase.execute(utilisateurId))
        .isInstanceOf(UtilisateurErreur.class)
        .hasMessage("Utilisateur non trouvé");
  }
}