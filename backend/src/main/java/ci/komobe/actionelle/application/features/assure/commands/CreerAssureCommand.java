package ci.komobe.actionelle.application.features.assure.commands;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Commande pour créer un assuré
 *
 * @author Moro KONÉ 2025-06-02
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreerAssureCommand {

  @NotBlank(message = "Le nom ne peut pas être vide")
  private String nom;

  @NotBlank(message = "Les prénoms ne peuvent pas être vides")
  private String prenoms;

  @NotBlank(message = "Le sexe ne peut pas être vide")
  private String sexe;

  @NotNull(message = "La date de naissance ne peut pas être null")
  @Past(message = "La date de naissance doit être dans le passé")
  private LocalDate dateNaissance;

  @NotBlank(message = "Le lieu de naissance ne peut pas être vide")
  private String lieuNaissance;

  @NotBlank(message = "Le numéro de carte d'identité ne peut pas être vide")
  private String numeroCarteIdentite;

  @NotBlank(message = "Le téléphone ne peut pas être vide")
  private String telephone;

  @NotBlank(message = "L'adresse ne peut pas être vide")
  private String adresse;

  @Email(message = "L'email doit être valide")
  @NotBlank(message = "L'email ne peut pas être vide")
  private String email;
}