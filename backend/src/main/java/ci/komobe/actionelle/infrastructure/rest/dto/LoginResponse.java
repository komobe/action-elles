package ci.komobe.actionelle.infrastructure.rest.dto;

import java.util.List;
import lombok.Builder;

/**
 * @author Moro KONÉ 2025-05-30
 */
@Builder
public record LoginResponse(
    String accessToken,
    String tokenType,
    Long expiresIn,
    LoginResponse.UserInfo user
) {

  @Builder
  public record UserInfo(String username, List<String> roles) {}
}


