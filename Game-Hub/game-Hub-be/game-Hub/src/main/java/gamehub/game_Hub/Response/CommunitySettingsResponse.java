package gamehub.game_Hub.Response;

import gamehub.game_Hub.Module.userSettings.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunitySettingsResponse {

  private Long settingId;
  private Long userId;
  private String settingName;
  private String description;
  private String access;

}
