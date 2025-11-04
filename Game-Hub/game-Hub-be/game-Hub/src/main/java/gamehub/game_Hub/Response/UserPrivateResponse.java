package gamehub.game_Hub.Response;

import java.util.List;
import java.util.Set;

import gamehub.game_Hub.Module.User.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivateResponse {

  private Long userId;
  private String firstName;
  private String lastName;
  private String email;
  private String username;
  private String bio;
  private LocationResponse location;
  private Status status;
  //   TODO add count of friends
  private int libraryCount;
  private int wishlistCount;
  private Set<BadgeResponse> badges;
  private Set<GameResponseShort> playRecently;
  private Set<GameResponseShort> favoriteGames;
  private Set<GenreResponse> favoriteGenres;
  private Set<GameResponseShort> recommendedGames;
  private byte [] userProfilePicture;
  private byte [] bannerImage;

}
