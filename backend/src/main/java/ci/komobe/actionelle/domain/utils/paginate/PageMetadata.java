package ci.komobe.actionelle.domain.utils.paginate;

import lombok.Builder;
import lombok.Value;

/**
 * Métadonnées d'une page de résultats
 * 
 * @author Moro KONÉ 2025-05-31
 */
@Value
@Builder
public class PageMetadata {
  /**
   * Numéro de la page courante (commence à 0)
   */
  int number;

  /**
   * Taille de la page
   */
  int size;

  /**
   * Nombre total d'éléments
   */
  long totalElements;

  /**
   * Nombre total de pages
   */
  int totalPages;

  /**
   * Crée les métadonnées à partir d'une page
   * 
   * @param <T>  type des éléments de la page
   * @param page la page source
   * @return les métadonnées
   */
  public static <T> PageMetadata from(Page<T> page) {
    return PageMetadata.builder()
        .number(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .build();
  }

  /**
   * Vérifie s'il existe une page suivante
   * 
   * @return true s'il existe une page suivante
   */
  public boolean hasNext() {
    return number < totalPages - 1;
  }

  /**
   * Vérifie s'il existe une page précédente
   * 
   * @return true s'il existe une page précédente
   */
  public boolean hasPrevious() {
    return number > 0;
  }

  /**
   * Vérifie si c'est la première page
   * 
   * @return true si c'est la première page
   */
  public boolean isFirst() {
    return !hasPrevious();
  }

  /**
   * Vérifie si c'est la dernière page
   * 
   * @return true si c'est la dernière page
   */
  public boolean isLast() {
    return !hasNext();
  }

  /**
   * Calcule l'offset de la page courante
   * 
   * @return l'offset calculé
   */
  public long getOffset() {
    return (long) number * size;
  }

  /**
   * Calcule le nombre d'éléments restants après la page courante
   * 
   * @return nombre d'éléments restants
   */
  public long getRemainingElements() {
    return Math.max(0, totalElements - (getOffset() + size));
  }

  /**
   * Calcule le nombre de pages restantes après la page courante
   * 
   * @return nombre de pages restantes
   */
  public int getRemainingPages() {
    return Math.max(0, totalPages - (number + 1));
  }
}