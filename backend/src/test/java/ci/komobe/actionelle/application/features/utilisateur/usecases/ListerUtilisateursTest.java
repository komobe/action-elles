package ci.komobe.actionelle.application.features.utilisateur.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.utils.paginate.Page;
import ci.komobe.actionelle.domain.utils.paginate.PageRequest;
import ci.komobe.actionelle.domain.valueobjects.Role;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ListerUtilisateurs")
class ListerUtilisateursTest {

  @Mock
  private UtilisateurRepository utilisateurRepository;

  @InjectMocks
  private ListerUtilisateurs useCase;

  @Test
  @DisplayName("devrait lister les utilisateurs avec pagination")
  void devraitListerLesUtilisateursAvecPagination() {
    // Given
    var pageRequest = PageRequest.of(0, 10);
    var utilisateur1 = new Utilisateur();
    utilisateur1.setUsername("user1");
    utilisateur1.setRole(Role.AMAZONE);

    var utilisateur2 = new Utilisateur();
    utilisateur2.setUsername("user2");
    utilisateur2.setRole(Role.ADMIN);

    var utilisateurs = List.of(utilisateur1, utilisateur2);
    var page = Page.<Utilisateur>builder()
        .data(utilisateurs)
        .number(0)
        .size(10)
        .totalElements(2)
        .totalPages(1)
        .build();

    when(utilisateurRepository.lister(pageRequest)).thenReturn(page);

    // When
    var result = useCase.executer(pageRequest);

    // Then
    assertThat(result.getData()).hasSize(2);
    assertThat(result.getData().get(0).getUsername()).isEqualTo("user1");
    assertThat(result.getData().get(1).getUsername()).isEqualTo("user2");
    assertThat(result.getTotalElements()).isEqualTo(2);
  }
}