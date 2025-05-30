package ci.komobe.actionelle.application.commands.assure;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author Moro KONÉ 2025-05-28
 */
@Getter
@SuperBuilder
public class AssureCommandBase {

  @NotBlank(message = "L'adresse ne peut pas être vide")
  private String adresse;

  @NotBlank(message = "Le téléphone ne peut pas être vide")
  private String telephone;

  @NotBlank(message = "Le nom ne peut pas être vide")
  private String nom;

  @NotBlank(message = "Le prénom ne peut pas être vide")
  private String prenom;

  @NotBlank(message = "Le numéro de carte d'identité ne peut pas être vide")
  private String numeroCarteIdentite;

  @NotBlank(message = "La ville ne peut pas être vide")
  private String ville;
}
