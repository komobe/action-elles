package ci.komobe.actionelle.domain.utils.paginate;

import lombok.Builder;
import lombok.Value;

/**
 * Représente une requête de pagination avec les paramètres de base : numéro de
 * page et taille
 * 
 * @author Moro KONÉ 2025-05-31
 */
@Value
@Builder
public class PageRequest implements Pageable {
  int number;
  int size;

  /**
   * Crée une requête de pagination avec les paramètres spécifiés
   * 
   * @param number numéro de la page (commence à 0)
   * @param size   nombre d'éléments par page
   * @return une nouvelle instance de PageRequest
   */
  public static PageRequest of(int number, int size) {
    return PageRequest.builder()
        .number(validatePageNumber(number))
        .size(validatePageSize(size))
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
    return PageRequest.of(number + 1, size);
  }

  @Override
  public PageRequest previous() {
    return hasPrevious() ? PageRequest.of(number - 1, size) : this;
  }

  @Override
  public PageRequest first() {
    return number == 0 ? this : PageRequest.of(0, size);
  }

  @Override
  public boolean hasPrevious() {
    return number > 0;
  }

  private static int validatePageNumber(int number) {
    if (number < 0) {
      return PaginationConstants.DEFAULT_PAGE_NUMBER;
    }
    return number;
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
}