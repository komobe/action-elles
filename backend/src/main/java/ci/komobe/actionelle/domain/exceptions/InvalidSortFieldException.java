package ci.komobe.actionelle.domain.exceptions;

/**
 * @author Moro KONÉ 2025-05-31
 */
public class InvalidSortFieldException extends RuntimeException {
  public InvalidSortFieldException(String message) {
    super(message);
  }
}