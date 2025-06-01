package ci.komobe.actionelle.domain.utils.sorting;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * Énumération des champs sur lesquels il est possible de trier
 * 
 * @author Moro KONÉ 2025-05-31
 */
@Getter
public enum SortableField {
  /**
   * Tri sur le nom d'utilisateur
   */
  USERNAME("username", "Nom d'utilisateur"),

  /**
   * Tri sur l'identifiant
   */
  ID("id", "Identifiant"),

  /**
   * Tri sur le rôle
   */
  ROLE("role", "Rôle");

  /**
   * Le nom du champ dans la base de données
   */
  private final String fieldName;

  /**
   * Le libellé du champ pour l'affichage
   */
  private final String label;

  /**
   * Cache des champs valides pour optimiser les recherches
   */
  private static final Map<String, SortableField> FIELD_MAP = Arrays.stream(values())
      .collect(Collectors.toMap(SortableField::getFieldName, field -> field));

  /**
   * Constructeur
   * 
   * @param fieldName le nom du champ dans la base de données
   * @param label     le libellé du champ pour l'affichage
   */
  SortableField(String fieldName, String label) {
    this.fieldName = fieldName;
    this.label = label;
  }

  /**
   * Vérifie si un champ est valide pour le tri
   * 
   * @param field le nom du champ à vérifier
   * @return true si le champ est valide, false sinon
   */
  public static boolean isValid(String field) {
    return field != null && FIELD_MAP.containsKey(field);
  }

  /**
   * Trouve un champ par son nom
   * 
   * @param fieldName le nom du champ
   * @return le champ correspondant ou vide si non trouvé
   */
  public static Optional<SortableField> fromFieldName(String fieldName) {
    return Optional.ofNullable(FIELD_MAP.get(fieldName));
  }

  /**
   * Liste tous les champs disponibles pour le tri
   * 
   * @return un tableau des noms de champs valides
   */
  public static String[] getValidFields() {
    return FIELD_MAP.keySet().toArray(new String[0]);
  }

  /**
   * Retourne une représentation lisible du champ
   * 
   * @return le libellé du champ
   */
  @Override
  public String toString() {
    return label;
  }
}