package gamehub.game_Hub.Request;

import gamehub.game_Hub.Module.User.Location;
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
public class UserUpdateRequest {

  private String firstName;
  private String lastName;
  private String email;
  private Location location;
  private String bio;


}
