package gamehub.game_Hub.Response;

import java.time.LocalDateTime;
import java.util.List;

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
  private String developer;
  private String publisher;
  private String description;
  private String releaseYear;
  private byte[] gameCoverImage;
  private boolean installed;
  private boolean favorite;
  private Integer playtimeMinutes;
  private Boolean currentlyPlaying;
  private List<GenreResponse> genres;
  private AgeRatingResponse ageRating;

}
