package gamehub.game_Hub.Response;

import java.util.List;
import java.util.Set;

import gamehub.game_Hub.enums.AccountStatus;
import gamehub.game_Hub.enums.Status;
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
  private Integer playTime;
  private Long reviews;
  private AccountStatus accountStatus;
  private CountryResponse country;
  private Status status;
  private String joinedDate;
  private int friendsCount;
  private int libraryCount;
  private int wishlistCount;
  private LevelResponse level;
  private GameResponseShort currentlyPlaying;
  private List<FriendProfileResponse> friends;
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
  private Boolean isFriend;
  private Boolean friendRequestSent;
  private Boolean friendRequestReceived;
}
