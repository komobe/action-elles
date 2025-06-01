package ci.komobe.actionelle.domain.utils.paginate;

import ci.komobe.actionelle.domain.utils.sorting.Sort;
import ci.komobe.actionelle.domain.utils.sorting.Sort.Direction;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Paramètres de pagination pour les requêtes HTTP
 * 
 * @author Moro KONÉ 2025-05-31
 */
@Value
@Builder
public class PaginationParams {
  /**
   * Numéro de la page (commence à 0)
   */
  @Min(0)
  @Builder.Default
  Integer page = PaginationConstants.DEFAULT_PAGE_NUMBER;

  /**
   * Taille de la page
   */
  @Min(PaginationConstants.MIN_PAGE_SIZE)
  @Max(PaginationConstants.MAX_PAGE_SIZE)
  @Builder.Default
  Integer size = PaginationConstants.DEFAULT_PAGE_SIZE;

  /**
   * Liste des tris
   */
  @Builder.Default
  List<Sort> sorts = new ArrayList<>();

  /**
   * Crée une instance avec les paramètres par défaut
   * 
   * @return instance avec paramètres par défaut
   */
  public static PaginationParams getDefault() {
    return PaginationParams.builder().build();
  }

  /**
   * Crée une instance à partir des paramètres HTTP
   * 
   * @param page numéro de page
   * @param size taille de page
   * @param sort champs de tri au format "field,direction"
   * @return instance créée
   */
  public static PaginationParams of(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size,
      @RequestParam(required = false) String sort
  ) {

    var params = PaginationParams.builder()
        .page(page)
        .size(size)
        .build();

    if (sort != null && !sort.isBlank()) {
      String[] parts = sort.split(",");
      String field = parts[0];
      Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
          ? Direction.DESC
          : Direction.ASC;
      params.sorts.add(Sort.by(field, direction));
    }

    return params;
  }

  /**
   * Convertit en PageRequest
   * 
   * @return PageRequest correspondant
   */
  public PageRequest toPageRequest() {
    return PageRequest.of(page, size, sorts);
  }
}