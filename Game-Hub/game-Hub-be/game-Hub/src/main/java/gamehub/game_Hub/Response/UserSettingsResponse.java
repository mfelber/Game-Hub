package gamehub.game_Hub.Response;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsResponse {

  private Long userId;
  private String firstName;
  private String lastName;
  private String userName;
  private String joinedDate;
  private LevelResponse level;
  private String email;
  private String bio;
  private CountryResponse country;
  private byte [] userProfilePicture;
  private byte [] bannerImage;
  private String bannerType;
  private String predefinedBannerPath;
  private String profileColor;
  private CardColorResponse cardColor;
  private List<GenreResponse> favoriteGenres;
  private UserLibraryResponse favoriteGame;
  private List<CommunitySettingsResponse> communitySettingsResponse;
  private List<StoreSettingsResponse> storeSettingsResponse;
  private List<PrivacySettingsResponse> privacySettingsResponse;
}
