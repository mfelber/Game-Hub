package gamehub.game_Hub.Service;

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
  private int libraryCount;
  private int wishlistCount;
  private Set<BadgeResponse> badges;
  private Set<GameResponseShort> playRecently;
  private Set<GameResponseShort> favoriteGames;
  private Set<GenreResponse> favoriteGenres;
  private byte [] userProfilePicture;
//   TODO add count of friends

}
