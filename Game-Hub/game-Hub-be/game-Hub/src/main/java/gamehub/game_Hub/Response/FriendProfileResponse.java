package gamehub.game_Hub.Response;

import gamehub.game_Hub.enums.Status;
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
public class FriendProfileResponse {

  private Long userId;
  private String userName;
  private byte [] userProfilePicture;
  private Status status;
  private GameResponseShort currentlyPlaying;
  private LevelResponse level;
  private String profileColor;

}
