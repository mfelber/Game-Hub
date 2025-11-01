package gamehub.game_Hub.Response;

import java.util.Set;

import gamehub.game_Hub.Module.User.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPublicResponse {

  private Long userId;
  private String username;
  private String bio;
  private Location location;
  //   TODO add count of friends
  private int libraryCount;
  private int wishlistCount;
  private Set<BadgeResponse> badges;
  private Set<GameResponseShort> playRecently;
  private Set<GameResponseShort> favoriteGames;
  private Set<GenreResponse> favoriteGenres;
  private Set<GameResponseShort> recommendedGames;
  private byte [] userProfilePicture;
}
