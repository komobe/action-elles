package ci.komobe.actionelle.application.commands;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Moro KONÉ 2025-05-29
 */
public record SimulerPrimeCommand(
    String produit,
    String categorie,
    int puissanceFiscale,
    LocalDate dateDeMiseEnCirculation,
    BigDecimal valeurNeuf,
    BigDecimal valeurVenale) {
}
