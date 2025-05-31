package ci.komobe.actionelle.application.features.devis.commands;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnregistrerDevisCommand extends SimulerPrimeCommand {

  @NotNull(message = "La référence est requise")
  private final String quoteReference;

  @NotNull(message = "La date d'expiration est requise")
  private final LocalDate endDate;

  @NotNull(message = "Le montant de la prime est requise")
  private final BigDecimal price;

  public LocalDate getDateExpiration() {
    return endDate;
  }
}
