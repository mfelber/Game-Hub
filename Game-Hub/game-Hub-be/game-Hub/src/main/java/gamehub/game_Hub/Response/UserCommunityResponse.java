package gamehub.game_Hub.Response;

import java.util.Set;

import gamehub.game_Hub.Module.User.Status;
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
public class UserCommunityResponse {

  private Long userId;
  private String username;
  private LocationResponse location;
  private Status status;
  private byte [] userProfilePicture;
  private String profileColor;


}
