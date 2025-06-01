package ci.komobe.actionelle.domain.utils.sorting;

import ci.komobe.actionelle.domain.exceptions.InvalidSortFieldException;
import ci.komobe.actionelle.domain.utils.paginate.PaginationConstants;
import java.util.Objects;
import lombok.Builder;
import lombok.Value;

/**
 * Représente un critère de tri avec une propriété et une direction
 * 
 * @author Moro KONÉ 2025-05-31
 */
@Value
@Builder
public class Sort {
  /**
   * Le nom de la propriété sur laquelle trier
   */
  String property;

  /**
   * La direction du tri (ASC ou DESC)
   */
  Direction direction;

  /**
   * Direction possible pour le tri
   */
  public enum Direction {
    /**
     * Tri ascendant (A vers Z, 0 vers 9)
     */
    ASC,

    /**
     * Tri descendant (Z vers A, 9 vers 0)
     */
    DESC;

    /**
     * Inverse la direction du tri
     * 
     * @return la direction opposée
     */
    public Direction reverse() {
      return this == ASC ? DESC : ASC;
    }
  }

  /**
   * Crée un tri sur une propriété avec une direction spécifiée
   * 
   * @param property  le nom de la propriété
   * @param direction la direction du tri
   * @return un nouveau tri
   * @throws InvalidSortFieldException si la propriété n'est pas valide
   * @throws IllegalArgumentException  si la direction est null
   */
  public static Sort by(String property, Direction direction) {
    if (!SortableField.isValid(property)) {
      throw new InvalidSortFieldException("Le champ '" + property + "' n'est pas un champ valide pour le tri");
    }
    Objects.requireNonNull(direction, "La direction de tri ne peut pas être null");

    return Sort.builder()
        .property(property)
        .direction(direction)
        .build();
  }

  /**
   * Crée un tri ascendant sur une propriété
   * 
   * @param property le nom de la propriété
   * @return un nouveau tri ascendant
   * @throws InvalidSortFieldException si la propriété n'est pas valide
   */
  public static Sort by(String property) {
    return by(property, Direction.ASC);
  }

  /**
   * Crée un tri par défaut (sur ID en ordre ascendant)
   * 
   * @return le tri par défaut
   */
  public static Sort getDefault() {
    return by(PaginationConstants.DEFAULT_SORT_FIELD.getFieldName(),
        PaginationConstants.DEFAULT_SORT_DIRECTION);
  }

  /**
   * Crée un nouveau tri avec la direction inversée
   * 
   * @return un nouveau tri avec la direction opposée
   */
  public Sort reverse() {
    return Sort.by(property, direction.reverse());
  }

  /**
   * Vérifie si le tri est ascendant
   * 
   * @return true si le tri est ascendant
   */
  public boolean isAscending() {
    return direction == Direction.ASC;
  }

  /**
   * Vérifie si le tri est descendant
   * 
   * @return true si le tri est descendant
   */
  public boolean isDescending() {
    return direction == Direction.DESC;
  }

  /**
   * Convertit le tri en chaîne de caractères pour SQL
   * 
   * @return la clause ORDER BY SQL correspondante
   */
  public String toSql() {
    return property + " " + direction.name();
  }
}