package gamehub.game_Hub.Response;

import java.time.LocalDateTime;

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
public class WishlistResponse {

  private LocalDateTime addedAt;
  private GamePreviewResponse game;

}
