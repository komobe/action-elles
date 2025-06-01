package ci.komobe.actionelle.infrastructure.rest.controllers.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

/**
 * Format standard des réponses de l'API
 *
 * @param <T> type des données de la réponse
 * @author Moro KONÉ 2025-05-31
 */
@Value
@Builder
public class ResponseApi<T> {

  /**
   * Indique si la requête a réussi
   */
  String status;

  /**
   * Données de la réponse
   */
  T data;

  /**
   * Message d'erreur éventuel
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Builder.Default
  String error = null;

  /**
   * Crée une réponse de succès
   *
   * @param <T>  type des données
   * @param data données de la réponse
   * @return réponse de succès
   */
  public static <T> ResponseApi<T> success(T data) {
    return ResponseApi.<T>builder()
        .status("success")
        .data(data)
        .build();
  }

  /**
   * Crée une réponse d'erreur
   *
   * @param <T>   type des données
   * @param error message d'erreur
   * @return réponse d'erreur
   */
  public static <T> ResponseApi<T> error(String error) {
    return ResponseApi.<T>builder()
        .status("success")
        .error(error)
        .build();
  }
}