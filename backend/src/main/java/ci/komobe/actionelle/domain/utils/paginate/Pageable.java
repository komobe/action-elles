package ci.komobe.actionelle.domain.utils.paginate;

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
}