package ci.komobe.actionelle.infrastructure.adapters.application.providers;

import ci.komobe.actionelle.application.commons.providers.PasswordProvider;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Component
public class PasswordProviderAdapter implements PasswordProvider {

  private final PasswordEncoder passwordEncoder;

  public PasswordProviderAdapter(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public String encode(String password) {
    return passwordEncoder.encode(password);
  }

  @Override
  public boolean matches(String password, String encodedPassword) {
    return passwordEncoder.matches(password, encodedPassword);
  }
}
