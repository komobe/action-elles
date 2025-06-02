package ci.komobe.actionelle.application.features.devis.commands;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

/**
 * Classe de base pour les commandes de devis
 * 
 * @author Moro KONÉ 2025-06-01
 */
@Data
public abstract class DevisCommandBase {
  @NotBlank(message = "La référence est obligatoire")
  private String reference;

  @NotBlank(message = "L'ID de l'assuré est obligatoire")
  private String assureId;

  @NotBlank(message = "L'ID du véhicule est obligatoire")
  private String vehiculeId;

  @NotBlank(message = "L'ID du produit est obligatoire")
  private String produitId;

  @NotNull(message = "La date de début est obligatoire")
  @Future(message = "La date de début doit être dans le futur")
  private LocalDate dateDebut;

  @NotNull(message = "La date de fin est obligatoire")
  @Future(message = "La date de fin doit être dans le futur")
  private LocalDate dateFin;

  @NotNull(message = "Le montant de la prime est obligatoire")
  @Min(value = 0, message = "Le montant de la prime doit être positif")
  private BigDecimal montantPrime;
}