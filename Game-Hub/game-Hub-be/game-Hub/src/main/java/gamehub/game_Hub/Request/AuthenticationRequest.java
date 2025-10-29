package gamehub.game_Hub.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {

  @Email(message = "Email is not formatted correctly")
  @NotEmpty(message = "Email must not be empty")
  @NotBlank(message = "Email must not be blank")
  private String email;

  @NotEmpty(message = "Password must not be empty")
  @NotBlank(message = "Password must not be blank")
  private String password;

}
