package ci.komobe.actionelle.domain.utils;

import static org.assertj.core.api.Assertions.assertThat;

import ci.komobe.actionelle.domain.entities.CategorieVehicule;
import ci.komobe.actionelle.domain.entities.Garantie;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.valueobjects.Role;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("FakeGenerator")
class FakeGeneratorTest {

  @Nested
  @DisplayName("generateVehicules")
  class GenerateVehiculesTest {

    @Test
    @DisplayName("devrait générer le nombre exact de véhicules demandé")
    void devraitGenererLeNombreExactDeVehicules() {
      // When
      List<Vehicule> vehicules = FakeGenerator.generateVehicules(5);

      // Then
      assertThat(vehicules).hasSize(5);
    }

    @Test
    @DisplayName("devrait générer des véhicules avec des données valides")
    void devraitGenererDesVehiculesValides() {
      // When
      List<Vehicule> vehicules = FakeGenerator.generateVehicules(1);
      Vehicule vehicule = vehicules.get(0);

      // Then
      assertThat(vehicule.getId()).isNotNull();
      assertThat(vehicule.getImmatriculation()).startsWith("CI-");
      assertThat(vehicule.getCouleur()).isNotEmpty();
      assertThat(vehicule.getNombreDePortes()).isBetween(2, 4);
      assertThat(vehicule.getNombreDeSieges()).isBetween(2, 6);
      assertThat(vehicule.getPuissanceFiscale()).isBetween(1, 100);
      assertThat(vehicule.getDateMiseEnCirculation()).isBefore(LocalDate.now().plusDays(1));
      assertThat(vehicule.getValeurNeuf()).isNotNull();
      assertThat(vehicule.getCategorie()).isNotNull();
    }
  }

  @Nested
  @DisplayName("generateCategorieVehicules")
  class GenerateCategorieVehiculesTest {

    @Test
    @DisplayName("devrait générer toutes les catégories de véhicules")
    void devraitGenererToutesLesCategories() {
      // When
      List<CategorieVehicule> categories = FakeGenerator.generateCategorieVehicules();

      // Then
      assertThat(categories).hasSize(4);
      assertThat(categories).extracting(CategorieVehicule::getCode)
          .containsExactlyInAnyOrder("201", "202", "203", "204");
    }

    @Test
    @DisplayName("devrait générer des catégories avec des données valides")
    void devraitGenererDesCategoriesValides() {
      // When
      List<CategorieVehicule> categories = FakeGenerator.generateCategorieVehicules();

      // Then
      assertThat(categories)
          .isNotEmpty()
          .allSatisfy(categorie -> {
            assertThat(categorie.getCode()).isNotEmpty();
            assertThat(categorie.getLibelle()).isNotEmpty();
            assertThat(categorie.getDescription()).isNotEmpty();
          });
    }
  }

  @Nested
  @DisplayName("generateProduits")
  class GenerateProduitsTest {

    @Test
    @DisplayName("devrait générer tous les produits attendus")
    void devraitGenererTousLesProduits() {
      // When
      List<Produit> produits = FakeGenerator.generateProduits();

      // Then
      assertThat(produits).hasSize(4);
      assertThat(produits).extracting(Produit::getNom)
          .containsExactlyInAnyOrder("Papillon", "Douby", "Douyou", "Toutourisquou");
    }

    @Test
    @DisplayName("devrait générer des produits avec les bonnes garanties")
    void devraitGenererDesProduitsAvecLesBonnesGaranties() {
      // When
      List<Produit> produits = FakeGenerator.generateProduits();

      // Then
      // Vérifier Papillon (RC, DOMMAGE, VOL)
      Produit papillon = produits.stream()
          .filter(p -> p.getNom().equals("Papillon"))
          .findFirst()
          .orElseThrow();
      verifierGaranties(papillon.getGaranties(), "RC", "DOMMAGE", "VOL");

      // Vérifier Toutourisquou (toutes les garanties)
      Produit toutourisquou = produits.stream()
          .filter(p -> p.getNom().equals("Toutourisquou"))
          .findFirst()
          .orElseThrow();
      verifierGaranties(toutourisquou.getGaranties(), "RC", "DOMMAGE", "VOL", "TIERCE_COLLISION",
          "TIERCE_PLAFONNEE",
          "INCENDIE");
    }

    private void verifierGaranties(List<Garantie> garanties, String... codesAttendus) {
      assertThat(garanties.stream().map(Garantie::getCode).distinct())
          .containsExactlyInAnyOrder(codesAttendus);
    }
  }

  @Nested
  @DisplayName("generateUtilisateurs")
  class GenerateUtilisateursTest {

    @Test
    @DisplayName("devrait générer tous les utilisateurs attendus")
    void devraitGenererTousLesUtilisateurs() {
      // When
      Map<String, Utilisateur> utilisateurs = FakeGenerator.generateUtilisateurs();

      // Then
      assertThat(utilisateurs).hasSize(5);
      assertThat(utilisateurs.keySet())
          .containsExactlyInAnyOrder("admin", "amazone", "gabriella", "stephanie", "robine");
    }

    @Test
    @DisplayName("devrait générer des utilisateurs avec les bons rôles")
    void devraitGenererDesUtilisateursAvecLesBonsRoles() {
      // When
      Map<String, Utilisateur> utilisateurs = FakeGenerator.generateUtilisateurs();

      // Then
      assertThat(utilisateurs.get("admin").getRole()).isEqualTo(Role.ADMIN);
      assertThat(utilisateurs.values())
          .filteredOn(u -> !u.getUsername().equals("admin"))
          .extracting(Utilisateur::getRole)
          .containsOnly(Role.AMAZONE);
    }

    @Test
    @DisplayName("devrait générer des utilisateurs avec des données valides")
    void devraitGenererDesUtilisateursValides() {
      // When
      Map<String, Utilisateur> utilisateurs = FakeGenerator.generateUtilisateurs();

      // Then
      assertThat(utilisateurs.values())
          .isNotEmpty()
          .allSatisfy(utilisateur -> {
            assertThat(utilisateur.getId()).isNotNull();
            assertThat(utilisateur.getUsername()).isNotEmpty();
            assertThat(utilisateur.getPassword()).isNotEmpty();
            assertThat(utilisateur.getRole()).isNotNull();
          });
    }
  }
}