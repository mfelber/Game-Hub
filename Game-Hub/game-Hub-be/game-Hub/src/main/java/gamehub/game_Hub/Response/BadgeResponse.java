package gamehub.game_Hub.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeResponse {

  private Long id;
  private String name;
  private String description;
  private String badgeImage;

}
