package ci.komobe.actionelle.domain.utils.paginate;

import ci.komobe.actionelle.domain.utils.sorting.Sort;
import ci.komobe.actionelle.domain.utils.sorting.Sort.Direction;
import ci.komobe.actionelle.domain.utils.sorting.SortableField;

/**
 * Constantes utilisées pour la pagination et le tri par défaut
 * 
 * @author Moro KONÉ 2025-05-31
 */
public final class PaginationConstants {
  private PaginationConstants() {
    // Empêche l'instanciation
  }

  /**
   * Numéro de page par défaut (première page)
   */
  public static final int DEFAULT_PAGE_NUMBER = 0;

  /**
   * Nombre d'éléments par page par défaut
   */
  public static final int DEFAULT_PAGE_SIZE = 10;

  /**
   * Nombre minimum d'éléments par page
   */
  public static final int MIN_PAGE_SIZE = 1;

  /**
   * Nombre maximum d'éléments par page
   */
  public static final int MAX_PAGE_SIZE = 100;

  /**
   * Champ de tri par défaut
   */
  public static final SortableField DEFAULT_SORT_FIELD = SortableField.ID;

  /**
   * Direction de tri par défaut
   */
  public static final Direction DEFAULT_SORT_DIRECTION = Direction.ASC;

  /**
   * Crée un tri par défaut
   * 
   * @return le tri par défaut (ID en ordre ascendant)
   */
  public static Sort getDefaultSort() {
    return Sort.by(DEFAULT_SORT_FIELD.getFieldName(), DEFAULT_SORT_DIRECTION);
  }
}