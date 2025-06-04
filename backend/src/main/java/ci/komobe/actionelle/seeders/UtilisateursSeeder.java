package ci.komobe.actionelle.seeders;

import ci.komobe.actionelle.domain.entities.Utilisateur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.domain.valueobjects.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Seeder pour les utilisateurs
 * 
 * @author Moro KONÉ 2025-06-03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UtilisateursSeeder {

  private final UtilisateurRepository repository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void seed() {
    log.info("Seeding des utilisateurs...");

    List<Utilisateur> utilisateurs = List.of(
        // Administrateur
        Utilisateur.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin"))
            .role(Role.ADMIN)
            .build(),

        // Agent commercial
        Utilisateur.builder()
            .username("agent")
            .password(passwordEncoder.encode("agent"))
            .role(Role.AMAZONE)
            .build(),

        // Utilisateur standard
        Utilisateur.builder()
            .username("user")
            .password(passwordEncoder.encode("user"))
            .role(Role.DEFAULT)
            .build());

    utilisateurs.forEach(utilisateur -> {
      if (!repository.existParUsername(utilisateur.getUsername())) {
        repository.enregistrer(utilisateur);
        log.info("Utilisateur créé : {}", utilisateur.getUsername());
      }
    });

    log.info("{} utilisateurs traités", utilisateurs.size());
  }
}