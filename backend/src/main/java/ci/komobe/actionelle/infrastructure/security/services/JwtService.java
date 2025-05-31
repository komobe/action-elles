package ci.komobe.actionelle.infrastructure.security.services;

import ci.komobe.actionelle.infrastructure.security.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service JWT avec gestion d'erreurs améliorée
 *
 * @author Moro KONÉ 2025-05-30
 */
@Service
@AllArgsConstructor
@Slf4j
public class JwtService {

  private final Key signingKey;
  private final JwtConfig jwtConfig;

  /**
   * Génère un token JWT pour un utilisateur
   */
  public String generateToken(String username) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + jwtConfig.getExpiration());

    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(now)
        .setExpiration(expiration)
        .signWith(signingKey, SignatureAlgorithm.HS512)
        .compact();
  }

  /**
   * Valide un token JWT
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(signingKey)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      log.debug("Token JWT invalide: {}", e.getMessage());
      return false;
    } catch (Exception e) {
      log.error("Erreur lors de la validation du token: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Extrait le nom d'utilisateur du token
   */
  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  /**
   * Vérifie si le token a expiré
   */
  public boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  /**
   * Extrait la date d'expiration du token
   */
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public long getTokenExpirationInMillis() {
    return jwtConfig.getExpiration();
  }

  /**
   * Extrait une claim spécifique du token
   */
  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Extrait toutes les claims du token
   */
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(signingKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public void blacklistToken(String token) {
    // TODO: A implementer
  }
}