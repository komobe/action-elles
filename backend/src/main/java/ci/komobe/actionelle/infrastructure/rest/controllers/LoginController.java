package ci.komobe.actionelle.infrastructure.rest.controllers;

import ci.komobe.actionelle.application.commons.providers.PasswordProvider;
import ci.komobe.actionelle.application.features.utilisateur.commands.InscriptionUtilisateurCommand;
import ci.komobe.actionelle.application.features.utilisateur.usecases.InscrireUtilisateur;
import ci.komobe.actionelle.domain.exceptions.UtilisateurErreur;
import ci.komobe.actionelle.domain.repositories.UtilisateurRepository;
import ci.komobe.actionelle.infrastructure.persistences.jpa.entities.UtilisateurEntity;
import ci.komobe.actionelle.infrastructure.persistences.jpa.repositories.UtilisateurJpaRepository;
import ci.komobe.actionelle.infrastructure.rest.dto.LoginRequest;
import ci.komobe.actionelle.infrastructure.rest.dto.LoginResponse;
import ci.komobe.actionelle.infrastructure.rest.dto.LoginResponse.UserInfo;
import ci.komobe.actionelle.infrastructure.security.services.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur d'authentification
 *
 * @author Moro KONÉ 2025-05-30
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentification", description = "API d'authentification et gestion des sessions")
@RequiredArgsConstructor
public class LoginController {

  private static final String AUTH_HEADER = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer ";

  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final UtilisateurJpaRepository utilisateurJpaRepository;
  private final UtilisateurRepository utilisateurRepository;
  private final PasswordProvider passwordProvider;


  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest request) {
    UtilisateurEntity user = utilisateurJpaRepository.findByUsername(request.username())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new UtilisateurErreur("Credentials incorrect");
    }

    String accessToken = jwtService.generateToken(user.getUsername());

    var userInfo = new UserInfo(
        user.getUsername(),
        List.of(user.getRole().name()));

    return new LoginResponse(
        accessToken,
        "Bearer",
        jwtService.getTokenExpirationInMillis(),
        userInfo);
  }

  @GetMapping("/profile")
  public UserInfo profile(HttpServletRequest request) {
    String authHeader = request.getHeader(AUTH_HEADER);
    if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
      String token = authHeader.substring(TOKEN_PREFIX.length());
      String username = jwtService.getUsernameFromToken(token);
      if (username != null) {
        return utilisateurJpaRepository.findByUsername(username)
            .map(u -> new UserInfo(u.getUsername(), List.of(u.getRole().name())))
            .orElseThrow(() -> new UtilisateurErreur("User not found"));
      }
    }
    throw new UtilisateurErreur("Invalide token, please login again.");
  }

  @PostMapping("/logout")
  public void logout(HttpServletRequest request) {
    String authHeader = request.getHeader(AUTH_HEADER);
    if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
      String token = authHeader.substring(TOKEN_PREFIX.length());
      jwtService.blacklistToken(token);
    }
  }

  @PostMapping("/register")
  public void register(@Valid @RequestBody InscriptionUtilisateurCommand command) {
    var useCase = new InscrireUtilisateur(utilisateurRepository, passwordProvider);
    useCase.executer(command);
  }
}
