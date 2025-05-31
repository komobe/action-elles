package ci.komobe.actionelle.application.commons.providers;

/**
 * @author Moro KONÉ 2025-05-30
 */
public interface PasswordProvider {
  String encode(String password);

  boolean matches(String password, String encodedPassword);
}
