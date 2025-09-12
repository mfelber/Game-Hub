package gamehub.game_Hub.Authentication;

import java.security.SecureRandom;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {

  private String token;

}
