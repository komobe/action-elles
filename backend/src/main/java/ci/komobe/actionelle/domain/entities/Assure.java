package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.exceptions.SouscriptionErreur;
import ci.komobe.actionelle.domain.valueobjects.Valeur;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité Assure
 *
 * @author Moro KONÉ 2025-06-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assure {

  private String id;
  private String numeroCarteIdentite;
  private String nom;
  private String prenoms;
  private String sexe;
  private LocalDate dateNaissance;
  private String lieuNaissance;
  private String email;
  private String telephone;
  private String adresse;
  private String profession;

  /**
   * Met à jour les informations modifiables de l'assuré. Les champs immuables
   * (nom, prenoms, lieu
   * de naissance, date naissance, numeroCarteIdentite) ne peuvent pas être
   * modifiés.
   *
   * @param assure L'assuré contenant les nouvelles informations
   * @throws IllegalArgumentException si on tente de modifier un champ immuable
   */
  public void mettreAjourDepuis(Assure assure) {
    if (assure == null) {
      return;
    }
    verifierInformationsIdentite(assure);
    mettreAJourInformationsContact(assure);
  }

  /**
   * Vérifie si l'assuré est différent de l'assuré fourni. La comparaison se fait
   * sur les champs
   * d'identification (nom, prenoms, numeroCarteIdentite).
   *
   * @param assure L'assuré à comparer
   * @return true si les assurés sont différents, false sinon
   */
  public boolean nonIdentiqueA(Assure assure) {
    if (assure == null) {
      return true;
    }

    return !Objects.equals(this.nom, assure.nom) ||
        !Objects.equals(this.prenoms, assure.prenoms) ||
        !Objects.equals(this.numeroCarteIdentite, assure.numeroCarteIdentite);
  }

  /**
   * Vérifie que les informations d'identité de l'assuré ne sont pas modifiées.
   * Ces informations
   * sont considérées comme immuables une fois l'assuré créé.
   *
   * @param assure L'assuré contenant les nouvelles informations
   * @throws IllegalArgumentException si une tentative de modification des
   *                                  informations d'identité
   *                                  est détectée
   */
  private void verifierInformationsIdentite(Assure assure) {
    if (assure == null) {
      return;
    }
    var erreurMap = new ArrayList<String>();
    if (assure.nom != null && !Objects.equals(this.nom, assure.nom)) {
      erreurMap.add("Le nom de l'assuré ne peut pas être modifié");
    }
    if (assure.prenoms != null && !Objects.equals(this.prenoms, assure.prenoms)) {
      erreurMap.add("Les prénoms de l'assuré ne peuvent pas être modifiés");
    }
    if (assure.numeroCarteIdentite != null && !Objects.equals(this.numeroCarteIdentite,
        assure.numeroCarteIdentite)) {
      erreurMap.add("Le numéro de carte d'identité ne peut pas être modifié");
    }
    if (assure.dateNaissance != null && !Objects.equals(this.dateNaissance, assure.dateNaissance)) {
      erreurMap.add("La date de naissance ne peut pas être modifiée");
    }
    if (assure.lieuNaissance != null && !Objects.equals(this.lieuNaissance, assure.lieuNaissance)) {
      erreurMap.add("Le lieu de naissance ne peut pas être modifié");
    }
    if (!erreurMap.isEmpty()) {
      throw new IllegalArgumentException(
          "Les informations d'identité de l'assuré ne peuvent pas être modifiées : "
              + String.join(", ", erreurMap));
    }
  }

  /**
   * Met à jour les informations de contact de l'assuré. Ces informations sont
   * modifiables et
   * comprennent l'email, le téléphone, la ville et l'adresse.
   *
   * @param assure L'assuré contenant les nouvelles informations de contact
   */
  private void mettreAJourInformationsContact(Assure assure) {
    if (assure == null) {
      return;
    }

    if (assure.email != null) {
      this.email = assure.email;
    }
    if (assure.telephone != null) {
      this.telephone = assure.telephone;
    }
    if (assure.adresse != null) {
      this.adresse = assure.adresse;
    }
    if (assure.sexe != null) {
      this.sexe = assure.sexe;
    }
    if (assure.profession != null) {
      this.profession = assure.profession;
    }
  }

  /**
   * Souscrit un véhicule à un produit pour l'assuré.
   *
   * @param vehicule             Le véhicule à souscrire
   * @param produit              Le produit de souscription
   * @param vehiculeValeurVenale La valeur vénale du véhicule
   * @return La souscription créée
   * @throws SouscriptionErreur si le véhicule n'est pas éligible au produit ou
   *                            si la valeur
   *                            vénale du véhicule est inférieure à 1
   */
  public Souscription souscrire(Vehicule vehicule, Produit produit,
      BigDecimal vehiculeValeurVenale) {
    if (!produit.estEligible(vehicule)) {
      throw new SouscriptionErreur("Le véhicule n'est pas éligible au produit");
    }

    if (vehiculeValeurVenale.compareTo(BigDecimal.ZERO) < 0) {
      throw new SouscriptionErreur(
          "La valeur vénale du véhicule ne peut pas être inférieure à 1 FCFA");
    }

    return Souscription.creer(this, vehicule, produit, Valeur.of(vehiculeValeurVenale, "XOF"));
  }
}
