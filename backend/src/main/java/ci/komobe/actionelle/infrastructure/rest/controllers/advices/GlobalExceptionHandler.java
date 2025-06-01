package ci.komobe.actionelle.infrastructure.rest.controllers.advices;

import ci.komobe.actionelle.domain.exceptions.InvalidSortFieldException;
import io.jsonwebtoken.JwtException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Moro KONÉ 2025-05-30
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ResponseApi<Void>> handleAccessDenied(AccessDeniedException ex) {
    return ResponseApi.forbidden("Accès refusé : " + ex.getMessage());
  }

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<ResponseApi<Void>> handleJwtException(JwtException ex) {
    return ResponseApi.unauthorized("Jeton invalide ou expiré : " + ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseApi<Map<String, String>>> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage())
    );
    return ResponseApi.error("Données invalides", errors);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ResponseApi<Void>> handleEmptyOrMalformedJson(
      HttpMessageNotReadableException ex) {
    return ResponseApi.error("Le format des données envoyées est invalide.");
  }

  @ExceptionHandler(InvalidSortFieldException.class)
  public ResponseEntity<ResponseApi<Void>> handleInvalidSortFieldException(
      InvalidSortFieldException ex) {
    return ResponseApi.error(ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ResponseApi<Void>> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    return ResponseApi.error(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseApi<Void>> handleAll(Exception ex) {
    return ResponseApi.error("Une erreur inattendue s'est produite.");
  }
}
