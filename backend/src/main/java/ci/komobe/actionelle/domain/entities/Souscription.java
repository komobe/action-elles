package ci.komobe.actionelle.domain.entities;

import ci.komobe.actionelle.domain.exceptions.SouscriptionErreur;
import ci.komobe.actionelle.domain.utils.IdGenerator;
import ci.komobe.actionelle.domain.valueobjects.StatutSouscription;
import ci.komobe.actionelle.domain.valueobjects.Valeur;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité Souscription
 *
 * @author Moro KONÉ 2025-06-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Souscription {

  private static final Random random = new Random();
  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
      "ddMMyyyy");

  private String id;
  private String numero;
  private LocalDate dateSouscription;
  private LocalDate dateExpiration;
  private Assure assure;
  private Vehicule vehicule;
  private Produit produit;
  private StatutSouscription statut;
  private Valeur vehiculeValeurVenale;

  /**
   * Crée une nouvelle souscription.
   *
   * @param assure               L'assuré
   * @param vehicule             Le véhicule
   * @param produit              Le produit
   * @param vehiculeValeurVenale La valeur vénale du véhicule
   * @return La souscription créée
   */
  public static Souscription creer(Assure assure, Vehicule vehicule, Produit produit,
      Valeur vehiculeValeurVenale) {
    return Souscription.builder()
        .id(IdGenerator.generateId())
        .assure(assure)
        .vehicule(vehicule)
        .produit(produit)
        .dateSouscription(LocalDate.now())
        .statut(StatutSouscription.EN_ATTENTE_VALIDATION)
        .numero(generateNumero())
        .vehiculeValeurVenale(vehiculeValeurVenale)
        .build();
  }

  private static String generateNumero() {
    String code = "SO";
    String date = LocalDate.now().format(dateTimeFormatter);
    int numeroRandom = random.nextInt(1_000_000_000);
    return code + date + numeroRandom;
  }

  public void setDateSouscription(LocalDate dateSouscription) {
    this.dateSouscription = dateSouscription;
    if (this.statut.estEnAttenteValidation() && this.dateExpiration == null) {
      this.dateExpiration = dateSouscription.plusYears(1);
    }
  }

  public void valider() {
    if (this.statut.estRejetee()) {
      throw new SouscriptionErreur(
          "Cette souscription a deja ete rejetée. Impossible de la validée à nouveau.");
    }

    if (this.statut.estValidee()) {
      throw new SouscriptionErreur(
          "Cette souscription a deja ete validée. Impossible de la validée à nouveau.");
    }

    this.statut = StatutSouscription.VALIDEE;
  }

  public void rejeter() {
    if (this.statut.estValidee()) {
      throw new SouscriptionErreur(
          "Cette souscription a deja ete validée. Impossible de la rejetée à nouveau.");
    }

    if (this.statut.estRejetee()) {
      throw new SouscriptionErreur(
          "Cette souscription a deja ete rejetée. Impossible de la rejetée à nouveau.");
    }

    this.statut = StatutSouscription.REJETEE;
  }
}