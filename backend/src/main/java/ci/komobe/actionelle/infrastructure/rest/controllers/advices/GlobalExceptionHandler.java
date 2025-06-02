package ci.komobe.actionelle.infrastructure.rest.controllers.advices;

import ci.komobe.actionelle.domain.exceptions.DomainErreur;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Moro KONÉ 2025-05-30
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DomainErreur.class)
  public ResponseEntity<ResponseApi<Void>> handleDomainError(DomainErreur ex) {
    return ResponseApi.error(ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ResponseApi<Void>> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    return ResponseApi.error(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseApi<Map<String, String>>> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return ResponseApi.error("Les données fournies sont invalides", errors);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ResponseApi<Void>> handleExpiredJwtException(ExpiredJwtException ex) {
    return ResponseApi.error(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseApi<Void>> handleAll(Exception ex) {
    return ResponseApi.error("Une erreur inattendue s'est produite.");
  }
}
