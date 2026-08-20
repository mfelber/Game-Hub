package gamehub.game_Hub.Response.Admin;

import java.time.LocalDateTime;

import gamehub.game_Hub.Module.Level;
import gamehub.game_Hub.Response.LevelResponse;
import gamehub.game_Hub.Response.LocationResponse;
import gamehub.game_Hub.enums.AccountStatus;
import gamehub.game_Hub.enums.AccountType;
import gamehub.game_Hub.enums.Location;
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
  private String profileColor;
  private String bio;
  private Role role;
  private AccountType accountType;
  private Long accountLevel;
  private LocationResponse location;
  private AccountStatus accountStatus;
  private LocalDateTime registered;
  private LocalDateTime lastLogin;
  private LocalDateTime lastModifiedAt;

}
