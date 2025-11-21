package gamehub.game_Hub.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LevelProgressResponse {

  private LevelResponse level;
  private Long currentXp;
  private Long requiredXp;
  private Long nextLevel;

}
