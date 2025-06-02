package ci.komobe.actionelle.domain.utils.paginate;

/**
 * Constantes utilisées pour la pagination
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
}