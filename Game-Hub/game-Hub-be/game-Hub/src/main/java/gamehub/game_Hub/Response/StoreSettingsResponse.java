package gamehub.game_Hub.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreSettingsResponse {

  private Long settingId;
  private Long userId;
  private String pegiRatingName;
  private String description;
  private boolean disabled;
}
