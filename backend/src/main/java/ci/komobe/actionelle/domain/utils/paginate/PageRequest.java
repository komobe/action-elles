package ci.komobe.actionelle.domain.utils.paginate;

import ci.komobe.actionelle.domain.utils.sorting.Sort;
import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Représente une requête de pagination avec les paramètres de base : numéro de
 * page, taille et tri
 * 
 * @author Moro KONÉ 2025-05-31
 */
@Value
@Builder
public class PageRequest implements Pageable {
  int number;
  int size;
  List<Sort> sorts;

  /**
   * Crée une requête de pagination avec les paramètres spécifiés
   * 
   * @param number numéro de la page (commence à 0)
   * @param size   nombre d'éléments par page
   * @return une nouvelle instance de PageRequest
   */
  public static PageRequest of(int number, int size) {
    return of(number, size, List.of(Sort.getDefault()));
  }

  /**
   * Crée une requête de pagination avec tri
   * 
   * @param number numéro de la page (commence à 0)
   * @param size   nombre d'éléments par page
   * @param sorts  liste des tris à appliquer
   * @return une nouvelle instance de PageRequest
   */
  public static PageRequest of(int number, int size, List<Sort> sorts) {
    return PageRequest.builder()
        .number(validatePageNumber(number))
        .size(validatePageSize(size))
        .sorts(validateSorts(sorts))
        .build();
  }

  /**
   * Crée une requête de pagination avec les paramètres par défaut
   * 
   * @return une nouvelle instance de PageRequest
   */
  public static PageRequest getDefault() {
    return of(PaginationConstants.DEFAULT_PAGE_NUMBER,
        PaginationConstants.DEFAULT_PAGE_SIZE);
  }

  @Override
  public long getOffset() {
    return (long) number * size;
  }

  @Override
  public PageRequest next() {
    return PageRequest.of(number + 1, size, sorts);
  }

  @Override
  public PageRequest previous() {
    return hasPrevious() ? PageRequest.of(number - 1, size, sorts) : this;
  }

  @Override
  public PageRequest first() {
    return number == 0 ? this : PageRequest.of(0, size, sorts);
  }

  @Override
  public boolean hasPrevious() {
    return number > 0;
  }

  private static int validatePageNumber(int number) {
    return number < 0 ? PaginationConstants.DEFAULT_PAGE_NUMBER : number;
  }

  private static int validatePageSize(int size) {
    if (size > PaginationConstants.MAX_PAGE_SIZE) {
      return PaginationConstants.MAX_PAGE_SIZE;
    }
    if (size < PaginationConstants.MIN_PAGE_SIZE) {
      return PaginationConstants.DEFAULT_PAGE_SIZE;
    }
    return size;
  }

  private static List<Sort> validateSorts(List<Sort> sorts) {
    return (sorts == null || sorts.isEmpty()) ? List.of(Sort.getDefault()) : sorts;
  }
}