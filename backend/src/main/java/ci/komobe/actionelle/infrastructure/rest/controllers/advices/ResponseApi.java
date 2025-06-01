package ci.komobe.actionelle.infrastructure.rest.controllers.advices;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Getter
public class ResponseApi<T> {

  public static final String SUCCESS_STATUS = "success";
  private final String status;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final T data;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private final String message;

  @JsonIgnore
  private final HttpStatus httpStatus;

  private final Map<String, Object> metadata;

  public ResponseApi(T data, String message, String status, HttpStatus httpStatus) {
    this(data, message, status, httpStatus, null);
  }

  public ResponseApi(
      T data, String message,
      String status, HttpStatus httpStatus,
      Map<String, Object> metadata
  ) {
    this.data = data;
    this.message = message;
    this.status = status;
    this.httpStatus = httpStatus;
    this.metadata = metadata;
  }

  // --- Success responses ---

  public static <T> ResponseEntity<ResponseApi<T>> success(T data) {
    return ResponseEntity.ok(new ResponseApi<>(data, null, SUCCESS_STATUS, HttpStatus.OK));


  }

  public static <T> ResponseEntity<ResponseApi<T>> success(T data, String message) {
    return ResponseEntity.ok(new ResponseApi<>(data, message, SUCCESS_STATUS, HttpStatus.OK));
  }

  public static <T> ResponseEntity<ResponseApi<T>> created(T data) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            new ResponseApi<>(data, "Ressource créée avec succès", SUCCESS_STATUS, HttpStatus.CREATED));
  }

  public static <T> ResponseEntity<ResponseApi<T>> noContent() {
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
        .body(new ResponseApi<>(null, null, SUCCESS_STATUS, HttpStatus.NO_CONTENT));
  }

  // --- Error responses ---

  public static <T> ResponseEntity<ResponseApi<T>> error(String message) {
    return error(message, null, HttpStatus.BAD_REQUEST);
  }

  public static <T> ResponseEntity<ResponseApi<T>> error(String message, HttpStatus status) {
    return error(message, null, status);
  }

  public static <T> ResponseEntity<ResponseApi<T>> error(String message, T data) {
    return error(message, data, HttpStatus.BAD_REQUEST);
  }

  public static <T> ResponseEntity<ResponseApi<T>> error(String message, T data,
      HttpStatus status) {
    return ResponseEntity.status(status)
        .body(new ResponseApi<>(data, message, "error", status));
  }

  public static <T> ResponseEntity<ResponseApi<T>> notFound(String message) {
    return error(message, HttpStatus.NOT_FOUND);
  }

  public static <T> ResponseEntity<ResponseApi<T>> forbidden(String message) {
    return error(message, HttpStatus.FORBIDDEN);
  }

  public static <T> ResponseEntity<ResponseApi<T>> unauthorized(String message) {
    return error(message, HttpStatus.UNAUTHORIZED);
  }

  public static <T> ResponseEntity<ResponseApi<T>> conflict(String message) {
    return error(message, HttpStatus.CONFLICT);
  }
}