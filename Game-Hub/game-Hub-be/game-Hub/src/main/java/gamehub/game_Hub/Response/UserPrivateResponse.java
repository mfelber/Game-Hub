package gamehub.game_Hub.Response;

import java.util.List;
import java.util.Set;

import gamehub.game_Hub.enums.Status;
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
  private Integer playTime;
  private Long reviews;
  private LocationResponse location;
  private Status status;
  private String joinedDate;
  private int friendsCount;
  private int libraryCount;
  private int wishlistCount;
  private int friendReqCount;
  private LevelResponse level;
  private GameResponseShort currentlyPlaying;
  private List<RecentGamesResponse> recentGamesResponse;
  private Set<BadgeResponse> badges;
  private Set<GenreResponse> favoriteGenres;
  private Set<GameResponseShort> recommendedGames;
  private UserLibraryResponse favoriteGame;
  private byte [] userProfilePicture;
  private byte [] bannerImage;
  private String bannerType;
  private String predefinedBannerPath;
  private String profileColor;
  private CardColorResponse cardColor;
}
