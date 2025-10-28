package gamehub.game_Hub.Request;

import java.util.Set;

import gamehub.game_Hub.Module.User.Location;
import gamehub.game_Hub.Response.GenreResponse;
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
  private String username;
  private String email;
  private Location location;
  private byte [] userProfilePicture;


}
