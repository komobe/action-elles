package ci.komobe.actionelle.application.services;

/**
 * @author Moro KONÉ 2025-05-30
 */
public interface AuthenticationStrategy {

  Object authenticate(String username, String credential);
}
