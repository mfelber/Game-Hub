package gamehub.game_Hub.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameResponseShort {

  private Long gameId;
  private String title;
  private byte[] gameCoverImage;

}
