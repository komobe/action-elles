package ci.komobe.actionelle.infrastructure.views.exceptionhandlers;

import ci.komobe.actionelle.infrastructure.views.dto.ResponseApi;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Moro KONÉ 2025-05-30
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ResponseApi<String>> handleAccessDenied(AccessDeniedException ex) {
    return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseApi<Map<String, String>>> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage())
    );

    ResponseApi<Map<String, String>> response = ResponseApi.error("Données invalides", errors);
    return ResponseEntity.unprocessableEntity().body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ResponseApi<String>> handleEmptyOrMalformedJson(
      HttpMessageNotReadableException ex) {
    return ResponseEntity
        .badRequest()
        .body(ResponseApi.error("Le format des données envoyées est invalide."));
  }


  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseApi<String>> handleAll(Exception ex) {
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  private ResponseEntity<ResponseApi<String>> buildResponse(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(ResponseApi.error(message));
  }
}
