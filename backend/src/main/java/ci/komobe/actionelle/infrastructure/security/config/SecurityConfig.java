package ci.komobe.actionelle.infrastructure.security.config;

import ci.komobe.actionelle.infrastructure.security.filters.JwtAuthenticationFilter;
import ci.komobe.actionelle.infrastructure.security.filters.RateLimitingFilter;
import ci.komobe.actionelle.infrastructure.security.properties.SecurityProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuration de sécurité pour l'application Actionelle
 *
 * @author Moro KONÉ 2025-05-30
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final RateLimitingFilter rateLimitingFilter;
  private final SecurityProperties securityProperties;
  private final String serverUrl;
  private final int serverPort;

  public SecurityConfig(
      JwtAuthenticationFilter jwtAuthenticationFilter,
      RateLimitingFilter rateLimitingFilter,
      SecurityProperties securityProperties,
      @Value("${server.url}") String serverUrl,
      @Value("${server.port}") int serverPort) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.rateLimitingFilter = rateLimitingFilter;
    this.securityProperties = securityProperties;
    this.serverUrl = serverUrl;
    this.serverPort = serverPort;
  }

  private static final String[] SWAGGER_WHITELIST = {
      "/v2/api-docs",
      "/v3/api-docs",
      "/v3/api-docs/**",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui/**",
      "/webjars/**",
      "/swagger-ui.html"
  };

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    // Configuration par défaut (tout refuser)
    CorsConfiguration defaultConfig = new CorsConfiguration();
    defaultConfig.setAllowedOrigins(Collections.emptyList());
    source.registerCorsConfiguration("/**", defaultConfig);

    // Configuration pour les serveurs externes uniquement
    securityProperties.getServers().forEach((serverName, serverConfig) -> {
      if (serverConfig.isEnabled()) {
        serverConfig.applyDefaults(securityProperties.getDefaultServerConfig());

        // Ne pas appliquer CORS si c'est la même origine
        String currentOrigin = String.format("%s:%d", serverUrl, serverPort);
        if (!serverConfig.getOrigin().equals(currentOrigin)) {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOrigins(List.of(serverConfig.getOrigin()));
          config.setAllowedMethods(serverConfig.getAllowedMethods());
          config.setAllowedHeaders(serverConfig.getAllowedHeaders());
          config.setExposedHeaders(serverConfig.getExposedHeaders());
          if (serverConfig.getMaxAge() != null) {
            config.setMaxAge(serverConfig.getMaxAge());
          }

          Map<String, List<String>> serverUrls = securityProperties.getUrlsForServer(serverName);
          serverUrls.keySet().forEach(pattern -> source.registerCorsConfiguration(pattern, config));
        }
      }
    });

    return source;
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    List<String> whiteList = new ArrayList<>(securityProperties.getWhiteList());
    whiteList.addAll(List.of(SWAGGER_WHITELIST));

    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(auth -> {
          // URLs en liste blanche (accessibles par tous)
          auth.requestMatchers(whiteList.toArray(new String[0])).permitAll();

          // Configuration des autorisations pour chaque serveur
          securityProperties.getServers().forEach((serverName, serverConfig) -> {
            if (serverConfig.isEnabled()) {
              Map<String, List<String>> serverUrls = securityProperties.getUrlsForServer(serverName);
              serverUrls.forEach((url, methods) -> {
                methods.forEach(method -> {
                  try {
                    auth.requestMatchers(HttpMethod.valueOf(method), url)
                        .authenticated();
                  } catch (Exception e) {
                    throw new RuntimeException(
                        String.format("Configuration invalide pour le serveur %s: méthode HTTP invalide %s",
                            serverName, method),
                        e);
                  }
                });
              });
            }
          });

          // Tous les autres endpoints nécessitent une authentification
          auth.anyRequest().authenticated();
        })
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint((request, response, authException) -> {
              response.setStatus(HttpStatus.UNAUTHORIZED.value());
              response.setContentType("application/json");
              response.setCharacterEncoding("UTF-8");
              response.getWriter().write(
                  String.format(
                      "{\"error\":\"Unauthorized\",\"message\":\"%s\",\"path\":\"%s\"}",
                      authException.getMessage(),
                      request.getRequestURI()));
            }));

    http.addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}