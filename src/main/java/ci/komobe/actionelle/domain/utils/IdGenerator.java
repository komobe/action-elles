package ci.komobe.actionelle.domain.utils;

/**
 * @author Moro KONÉ 2025-05-28
 */
public class IdGenerator {

  private IdGenerator() {
  }

  public static String generateId() {
    return java.util.UUID.randomUUID().toString();
  }
}
