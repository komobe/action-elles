package ci.komobe.actionelle.infrastructure.views.exceptionhandlers;

import ci.komobe.actionelle.infrastructure.views.dto.ResponseApi;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<ResponseApi<String>> handleValidation(MethodArgumentNotValidException ex) {
    // Exemple d’extraction des messages d’erreur de validation
    String errorMessage = ex.getBindingResult().getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.joining(", "));
    return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, errorMessage);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseApi<String>> handleAll(Exception ex) {
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  private ResponseEntity<ResponseApi<String>> buildResponse(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(ResponseApi.error(message));
  }
}
