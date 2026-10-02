package gamehub.game_Hub.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivacySettingsResponse {

  private Long settingId;
  private Long userId;
  private String settingName;
  private String description;
  private String access;

}
