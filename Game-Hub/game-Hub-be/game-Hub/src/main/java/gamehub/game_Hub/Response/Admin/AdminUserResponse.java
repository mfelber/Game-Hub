package gamehub.game_Hub.Response.Admin;

import java.time.LocalDateTime;

import gamehub.game_Hub.enums.AccountStatus;
import gamehub.game_Hub.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUserResponse {

  private Long userId;
  private String firstName;
  private String lastName;
  private String userName;
  private String email;
  private byte[] profilePicture;
  private Role role;
  private AccountStatus accountStatus;
  private LocalDateTime registered;
  private LocalDateTime lastLogin;

}
