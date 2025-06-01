package ci.komobe.actionelle.domain.utils.paginate;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Représente une page de résultats paginés
 * 
 * @param <T> le type des éléments contenus dans la page
 * @author Moro KONÉ 2025-05-31
 */
@Value
@Builder
public class Page<T> {
  /**
   * Les données de la page courante
   */
  List<T> data;

  /**
   * Le numéro de la page courante (commence à 0)
   */
  int number;

  /**
   * Le nombre d'éléments par page
   */
  int size;

  /**
   * Le nombre total d'éléments dans toutes les pages
   */
  long totalElements;

  /**
   * Le nombre total de pages
   */
  int totalPages;

  /**
   * Vérifie s'il existe une page suivante
   * 
   * @return true s'il existe une page suivante, false sinon
   */
  public boolean hasNext() {
    return number < totalPages - 1;
  }

  /**
   * Vérifie s'il existe une page précédente
   * 
   * @return true s'il existe une page précédente, false sinon
   */
  public boolean hasPrevious() {
    return number > 0;
  }

  public boolean isFirst() {
    return !hasPrevious();
  }

  public boolean isLast() {
    return !hasNext();
  }

  public boolean hasContent() {
    return data != null && !data.isEmpty();
  }

  public int getNumberOfElements() {
    return data == null ? 0 : data.size();
  }
}