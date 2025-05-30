package ci.komobe.actionelle.infrastructure.services;

import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.UserJpaEntity;
import ci.komobe.actionelle.infrastructure.hibernatejpa.repositories.UserJpaRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserJpaRepository userJpaRepository;

  public CustomUserDetailsService(UserJpaRepository userJpaRepository) {
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserJpaEntity user = userJpaRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Utitisateur non trouvé"));

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        List.of(new SimpleGrantedAuthority(user.getRole().name()))
    );
  }
}
