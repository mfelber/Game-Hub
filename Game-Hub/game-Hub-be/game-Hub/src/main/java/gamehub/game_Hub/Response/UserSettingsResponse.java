package gamehub.game_Hub.Response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsResponse {
  private List<CommunitySettingsResponse> communitySettingsResponse;
  private List<StoreSettingsResponse> storeSettingsResponse;
  private List<PrivacySettingsResponse> privacySettingsResponse;
}
