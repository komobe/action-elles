package ci.komobe.actionelle.application.commands;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Moro KONÉ 2025-05-29
 */
public record SimulerPrimeCommand(
    @NotBlank(message = "Le produit est requis")
    String produit,

    @NotBlank(message = "La catégorie est requise")
    String categorie,

    @Min(value = 1, message = "La puissance fiscale doit être supérieure à 0")
    @Max(value = 100, message = "La puissance fiscale doit être inférieure à 100")
    int puissanceFiscale,

    @NotNull(message = "La date de mise en circulation est requise")
    LocalDate dateDeMiseEnCirculation,

    @NotNull(message = "La valeur neuf est requise")
    BigDecimal valeurNeuf,

    @NotNull(message = "La valeur venale est requise")
    BigDecimal valeurVenale
) {}
