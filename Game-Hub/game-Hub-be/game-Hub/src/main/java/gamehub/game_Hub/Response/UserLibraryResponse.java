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
public class UserLibraryResponse {

  private Long gameId;
  private String title;
  private byte[] gameCoverImage;
  private boolean installed;
  private boolean favorite;
  private Integer playtimeMinutes;
  private LocalDateTime lastPlayed;

}
