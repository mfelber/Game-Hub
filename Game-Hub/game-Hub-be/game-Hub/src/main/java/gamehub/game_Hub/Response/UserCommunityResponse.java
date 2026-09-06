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
public class UserCommunityResponse {

  private Long userId;
  private String username;
  private LocationResponse location;
  private String joinedDate;
  private int friendsCount;
  private int libraryCount;
  // TODO when impleneting reviews
  private int reviewsCount;
  private Status status;
  private byte [] userProfilePicture;
  private String profileColor;
  private Boolean isFriend;
  private Boolean friendRequestSent;
  private Boolean friendRequestReceived;

}
