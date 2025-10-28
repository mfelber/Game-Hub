package gamehub.game_Hub.Service;

import gamehub.game_Hub.Module.User.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

  private String firstName;
  private String lastName;
  private String email;
  private Location location;
  private String bio;

}
