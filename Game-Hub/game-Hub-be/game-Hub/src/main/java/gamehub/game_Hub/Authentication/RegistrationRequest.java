package gamehub.game_Hub.Authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

  @NotEmpty(message = "First name must not be empty")
  @NotBlank(message = "First name must not be blank")
  private String firstName;

  @NotEmpty(message = "Last name must not be empty")
  @NotBlank(message = "Last name must not be blank")
  private String lastName;

  @NotEmpty(message = "Username must not be empty")
  @NotBlank(message = "Username must not be blank")
  private String username;

  @Email(message = "Email is not formatted correctly")
  @NotEmpty(message = "Email must not be empty")
  @NotBlank(message = "Email must not be blank")
  private String email;

  @NotEmpty(message = "Password must not be empty")
  @NotBlank(message = "Password must not be blank")
  @Size(min = 8, message = "Password must be at least 8 characters long")
  private String password;

}
