package ci.komobe.actionelle.infrastructure.configs;

import ci.komobe.actionelle.infrastructure.filters.JwtAuthenticationFilter;
import ci.komobe.actionelle.infrastructure.filters.RateLimitingFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration de sécurité pour l'application Actionelle
 * @author Moro KONÉ 2025-05-30
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final RateLimitingFilter rateLimitingFilter;

  public SecurityConfig(
      JwtAuthenticationFilter jwtAuthFilter,
      RateLimitingFilter rateLimitingFilter
  ) {
    this.jwtAuthenticationFilter = jwtAuthFilter;
    this.rateLimitingFilter = rateLimitingFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // Désactiver CSRF pour API REST
        .csrf(AbstractHttpConfigurer::disable)

        // Configuration des autorisations
        .authorizeHttpRequests(auth -> auth
            // Endpoints publics
            .requestMatchers("/api/login", "/api/logout").permitAll()

            // Tous les autres endpoints nécessitent une authentification
            .anyRequest().authenticated()
        )

        // Configuration de session - Stateless pour JWT
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        // Désactiver l'authentification HTTP Basic et Form Login
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)

        // Gestion des exceptions d'authentification
        .exceptionHandling(exception ->
            exception.authenticationEntryPoint((request, response, authException) -> {
              response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
              response.setContentType("application/json");
              response.setCharacterEncoding("UTF-8");
              response.getWriter().write(
                  String.format(
                      "{\"error\":\"Unauthorized\",\"message\":\"%s\",\"path\":\"%s\"}",
                      authException.getMessage(),
                      request.getRequestURI()
                  )
              );
            })
        );

    // 1. RateLimitingFilter en premier pour limiter les requêtes
    http.addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);

    // 2. JwtAuthenticationFilter pour l'authentification JWT
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}