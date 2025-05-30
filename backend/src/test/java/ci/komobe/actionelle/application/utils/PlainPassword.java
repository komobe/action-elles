package ci.komobe.actionelle.application.utils;

import ci.komobe.actionelle.application.providers.PasswordProvider;

/**
 * @author Moro KONÉ 2025-05-30
 */
public class PlainPassword implements PasswordProvider {

  @Override
  public String encode(String password) {
    return password;
  }

  @Override
  public boolean matches(String password, String encodedPassword) {
    return password.equals(encodedPassword);
  }
}
