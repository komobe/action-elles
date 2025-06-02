package ci.komobe.actionelle.application.features.souscription.dto;

import ci.komobe.actionelle.domain.entities.Assure;
import ci.komobe.actionelle.domain.entities.Produit;
import ci.komobe.actionelle.domain.entities.Vehicule;
import ci.komobe.actionelle.domain.valueobjects.Valeur;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

/**
 * DTO pour l'attestation
 * 
 * @author Moro KONÉ 2025-06-01
 */
@Data
@Builder
public class AttestationDto {
  private String souscriptionId;
  private String numero;
  private LocalDate dateSouscription;
  private LocalDate dateExpiration;
  private Assure assure;
  private Vehicule vehicule;
  private Produit produit;
  private Valeur vehiculeValeurVenale;
}