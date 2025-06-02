package ci.komobe.actionelle.application.features.produit.commands;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Commande pour modifier un produit
 *
 * @author Moro KONÉ 2025-06-01
 */
@Getter
@SuperBuilder
public class ModifierProduitCommand extends ProduitCommandBase {

  @NotNull(message = "L'identifiant est obligatoire")
  private final String id;
}