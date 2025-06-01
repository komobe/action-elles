package ci.komobe.actionelle.domain.utils.paginate;

import ci.komobe.actionelle.domain.utils.sorting.Sort;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface pour les objets paginables
 * 
 * @author Moro KONÉ 2025-05-31
 */
public interface Pageable {
  /**
   * Obtient le numéro de la page
   * 
   * @return numéro de la page (commence à 0)
   */
  int getNumber();

  /**
   * Obtient la taille de la page
   * 
   * @return nombre d'éléments par page
   */
  int getSize();

  /**
   * Obtient l'offset pour la pagination SQL
   * 
   * @return offset calculé
   */
  long getOffset();

  /**
   * Obtient les critères de tri
   * 
   * @return liste des tris à appliquer
   */
  List<Sort> getSorts();

  /**
   * Vérifie si la pagination est active
   * 
   * @return true si la pagination est active
   */
  default boolean isPaged() {
    return true;
  }

  /**
   * Crée une nouvelle pagination pour la page suivante
   * 
   * @return nouvelle pagination
   */
  Pageable next();

  /**
   * Crée une nouvelle pagination pour la page précédente
   * 
   * @return nouvelle pagination ou la même si première page
   */
  Pageable previous();

  /**
   * Crée une nouvelle pagination pour la première page
   * 
   * @return nouvelle pagination
   */
  Pageable first();

  /**
   * Vérifie s'il existe une page précédente
   * 
   * @return true s'il existe une page précédente
   */
  boolean hasPrevious();

  /**
   * Convertit les critères de tri en clause SQL ORDER BY
   * 
   * @return clause SQL ORDER BY
   */
  default String toSqlOrderBy() {
    List<Sort> sorts = getSorts();
    if (sorts == null || sorts.isEmpty()) {
      return Sort.getDefault().toSql();
    }
    return sorts.stream()
        .map(Sort::toSql)
        .collect(Collectors.joining(", "));
  }

  /**
   * Vérifie si la page actuelle est la première
   * 
   * @return true si c'est la première page
   */
  default boolean isFirst() {
    return !hasPrevious();
  }

  /**
   * Crée une nouvelle pagination avec une taille de page différente
   * 
   * @param newSize nouvelle taille de page
   * @return nouvelle pagination
   */
  default Pageable withSize(int newSize) {
    return PageRequest.of(getNumber(), newSize, getSorts());
  }

  /**
   * Crée une nouvelle pagination avec un numéro de page différent
   * 
   * @param newNumber nouveau numéro de page
   * @return nouvelle pagination
   */
  default Pageable withNumber(int newNumber) {
    return PageRequest.of(newNumber, getSize(), getSorts());
  }
}