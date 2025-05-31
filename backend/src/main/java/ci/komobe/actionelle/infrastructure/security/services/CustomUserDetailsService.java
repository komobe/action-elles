package ci.komobe.actionelle.infrastructure.security.services;

import ci.komobe.actionelle.infrastructure.persistences.postgres.entities.UtilisateurEntity;
import ci.komobe.actionelle.infrastructure.persistences.postgres.repositories.UtilisateurJpaRepository;
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

  private final UtilisateurJpaRepository utilisateurJpaRepository;

  public CustomUserDetailsService(UtilisateurJpaRepository utilisateurJpaRepository) {
    this.utilisateurJpaRepository = utilisateurJpaRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UtilisateurEntity user = utilisateurJpaRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Utitisateur non trouvé"));

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        List.of(new SimpleGrantedAuthority(user.getRole().name()))
    );
  }
}
