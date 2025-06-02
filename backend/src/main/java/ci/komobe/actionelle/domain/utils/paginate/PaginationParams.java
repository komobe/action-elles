package ci.komobe.actionelle.domain.utils.paginate;

import static ci.komobe.actionelle.domain.utils.paginate.PaginationConstants.MAX_PAGE_SIZE;
import static ci.komobe.actionelle.domain.utils.paginate.PaginationConstants.MIN_PAGE_SIZE;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Paramètres de pagination pour les requêtes HTTP La page commence à 1 pour le front-end, mais est
 * convertie en 0-based pour JPA
 *
 * @author Moro KONÉ 2025-05-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationParams {

  @NotNull(message = "Le numéro de page est obligatoire")
  @Min(value = 1, message = "Le numéro de page doit être supérieur ou égal à 1")
  private Integer page;

  @NotNull(message = "La taille de page est obligatoire")
  @Min(
      value = MIN_PAGE_SIZE,
      message = "La taille de page doit être supérieure ou égale à " + MIN_PAGE_SIZE
  )
  @Max(
      value = MAX_PAGE_SIZE,
      message = "La taille de page doit être inférieure ou égale à " + MAX_PAGE_SIZE
    )
  private Integer size;

  public PageRequest toPageRequest() {
    if (page == null || size == null) {
      throw new IllegalArgumentException("Les paramètres de pagination sont obligatoires");
    }
    // Convertir la page 1-based en 0-based pour JPA
    return PageRequest.of(page - 1, size);
  }
}
