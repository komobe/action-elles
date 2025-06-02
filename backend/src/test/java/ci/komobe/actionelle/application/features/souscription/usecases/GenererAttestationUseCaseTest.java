package ci.komobe.actionelle.application.features.souscription.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ci.komobe.actionelle.application.commons.services.attestation.AttestationFormat;
import ci.komobe.actionelle.application.commons.services.attestation.AttestationGeneratorService;
import ci.komobe.actionelle.application.features.souscription.dto.AttestationDto;
import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.entities.Souscription;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.exceptions.SouscriptionErreur;
import ci.komobe.actionelle.domain.repositories.SouscriptionRepository;
import ci.komobe.actionelle.domain.valueobjects.Valeur;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("GenererAttestationUseCase")
class GenererAttestationUseCaseTest {

  @Mock
  private SouscriptionRepository souscriptionRepository;

  @Mock
  private AttestationGeneratorService attestationGeneratorService;

  @Captor
  private ArgumentCaptor<AttestationDto> attestationDtoCaptor;

  private GenererAttestationUseCase useCase;

  @BeforeEach
  void setUp() {
    useCase = new GenererAttestationUseCase(souscriptionRepository, attestationGeneratorService);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = { " ", "   " })
  @DisplayName("La génération d'attestation échoue avec un numéro de souscription invalide")
  void generationAttestationEchoueAvecNumeroInvalide(String numeroInvalide) {
    // When & Then
    assertThatThrownBy(() -> useCase.execute(numeroInvalide, AttestationFormat.PDF))
        .isInstanceOf(SouscriptionErreur.class)
        .hasMessage("La souscription " + numeroInvalide + " n'existe pas");
  }

  @Test
  @DisplayName("La génération d'attestation échoue car souscription non trouvée")
  void generationAttestationEchoueCarSouscriptionNonTrouvee() {
    // Given
    String numero = "SOUSCRIPTION-001";
    when(souscriptionRepository.chercherParNumero(numero)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> useCase.execute(numero, AttestationFormat.PDF))
        .isInstanceOf(SouscriptionErreur.class);
  }

  @Test
  @DisplayName("La génération d'attestation échoue car format non supporté")
  void generationAttestationEchoueCarFormatNonSupporte() {
    // Given
    String numero = "SOUSCRIPTION-001";
    var souscription = creerSouscriptionTest(numero);
    when(souscriptionRepository.chercherParNumero(numero)).thenReturn(Optional.of(souscription));
    when(attestationGeneratorService.generer(any(), any()))
        .thenThrow(new IllegalArgumentException("Format non supporté"));

    // When & Then
    assertThatThrownBy(() -> useCase.execute(numero, AttestationFormat.PDF))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("La génération d'attestation se fait avec succès")
  void generationAttestationSeFaitAvecSucces() {
    // Given
    String numero = "SOUSCRIPTION-001";
    byte[] expectedPdf = "PDF content".getBytes();
    var souscription = creerSouscriptionTest(numero);

    when(souscriptionRepository.chercherParNumero(numero)).thenReturn(Optional.of(souscription));
    when(attestationGeneratorService.generer(any(), any())).thenReturn(expectedPdf);

    // When
    byte[] result = useCase.execute(numero, AttestationFormat.PDF);

    // Then
    assertThat(result).isEqualTo(expectedPdf);

    verify(attestationGeneratorService).generer(attestationDtoCaptor.capture(), any());
    AttestationDto attestationDto = attestationDtoCaptor.getValue();

    assertThat(attestationDto)
        .isNotNull()
        .satisfies(dto -> {
          assertThat(dto.getSouscriptionId()).isEqualTo(numero);
          assertThat(dto.getNumero()).isEqualTo(numero);
          assertThat(dto.getVehicule()).isEqualTo(souscription.getVehicule());
          assertThat(dto.getVehiculeValeurVenale()).isEqualTo(souscription.getVehiculeValeurVenale());
          assertThat(dto.getProduit()).isEqualTo(souscription.getProduit());
        });
  }

  private Souscription creerSouscriptionTest(String numero) {
    var assure = Assure.builder()
        .numeroCarteIdentite("CI123")
        .nom("Doe")
        .prenoms("John")
        .dateNaissance(LocalDate.now().minusYears(30))
        .lieuNaissance("Abidjan")
        .email("john.doe@example.com")
        .telephone("+2250123456789")
        .adresse("Abidjan")
        .build();

    var categorie = CategorieVehicule.builder()
        .code("201")
        .libelle("Promenade et Affaire")
        .description("Usage personnel")
        .build();

    var vehicule = Vehicule.builder()
        .immatriculation("ABC123")
        .couleur("Rouge")
        .nombreDeSieges(4)
        .nombreDePortes(5)
        .categorie(categorie)
        .dateMiseEnCirculation(LocalDate.now().minusYears(2))
        .puissanceFiscale(10)
        .valeurNeuf(Valeur.of(BigDecimal.valueOf(10_000_000), "XOF"))
        .build();

    var produit = Produit.builder()
        .nom("Papillon")
        .description("RC, DOMMAGE, VOL")
        .build();

    return Souscription.builder()
        .numero(numero)
        .dateSouscription(LocalDate.now())
        .assure(assure)
        .vehicule(vehicule)
        .produit(produit)
        .vehiculeValeurVenale(Valeur.of(BigDecimal.valueOf(8_000_000), "XOF"))
        .build();
  }
}