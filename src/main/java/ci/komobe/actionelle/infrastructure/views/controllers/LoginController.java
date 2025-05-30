package ci.komobe.actionelle.infrastructure.views.controllers;

import ci.komobe.actionelle.application.exceptions.UtilisateurError;
import ci.komobe.actionelle.infrastructure.hibernatejpa.entities.UtilisateurJpaEntity;
import ci.komobe.actionelle.infrastructure.hibernatejpa.repositories.UtilisateurJpaRepository;
import ci.komobe.actionelle.infrastructure.services.JwtService;
import ci.komobe.actionelle.infrastructure.views.dto.LoginRequest;
import ci.komobe.actionelle.infrastructure.views.dto.LoginResponse;
import ci.komobe.actionelle.infrastructure.views.dto.LoginResponse.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Moro KONÉ 2025-05-30
 */
@RestController
@RequestMapping("/api")
public class LoginController {

  private static final String AUTH_HEADER = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer ";

  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final UtilisateurJpaRepository utilisateurJpaRepository;

  public LoginController(
      PasswordEncoder passwordEncoder, JwtService jwtService,
      UtilisateurJpaRepository utilisateurJpaRepository
  ) {
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.utilisateurJpaRepository = utilisateurJpaRepository;
  }

  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest request) {
    UtilisateurJpaEntity user = utilisateurJpaRepository.findByUsername(request.username())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new UtilisateurError("Credentials incorrect");
    }

    String accessToken = jwtService.generateToken(user.getUsername());

    UserInfo userInfo = UserInfo.builder()
        .username(user.getUsername())
        .roles(List.of(user.getRole().name()))
        .build();

    return LoginResponse.builder()
        .accessToken(accessToken)
        .tokenType("Bearer")
        .expiresIn(jwtService.getTokenExpirationInMillis())
        .user(userInfo
        ).build();
  }

  @PostMapping("/logout")
  public void logout(HttpServletRequest request) {
    String authHeader = request.getHeader(AUTH_HEADER);
    if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
      String token = authHeader.substring(TOKEN_PREFIX.length());
      jwtService.blacklistToken(token);
    }
  }
}
