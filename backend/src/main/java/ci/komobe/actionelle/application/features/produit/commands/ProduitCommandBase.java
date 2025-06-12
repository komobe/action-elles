package ci.komobe.actionelle.application.features.produit.commands;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Classe de base pour les commandes de produit
 * 
 * @author Moro KONÉ 2025-06-01
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class ProduitCommandBase {
  @NotBlank(message = "Le code est obligatoire")
  @Pattern(regexp = "^[A-Z]{2}\\d{4}$", message = "Le code doit être composé de 2 lettres majuscules suivies de 4 chiffres")
  private String code;

  @NotBlank(message = "Le libellé est obligatoire")
  @Size(min = 3, max = 100, message = "Le libellé doit contenir entre 3 et 100 caractères")
  private String libelle;

  @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
  private String description;

  @NotNull(message = "Le taux de commission est obligatoire")
  @DecimalMin(value = "0.0", message = "Le taux de commission doit être supérieur ou égal à 0")
  @DecimalMax(value = "100.0", message = "Le taux de commission doit être inférieur ou égal à 100")
  private BigDecimal tauxCommission;

  @NotNull(message = "Le taux de prime est obligatoire")
  @DecimalMin(value = "0.0", message = "Le taux de prime doit être supérieur ou égal à 0")
  @DecimalMax(value = "100.0", message = "Le taux de prime doit être inférieur ou égal à 100")
  private BigDecimal tauxPrime;

  @NotNull(message = "Les garanties sont obligatoires")
  private List<String> garanties;

  @NotNull(message = "Les catégories de véhicules sont obligatoires")
  private List<String> categoriesVehicules;
}