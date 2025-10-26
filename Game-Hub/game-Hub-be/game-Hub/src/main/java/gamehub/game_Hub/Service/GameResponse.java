package gamehub.game_Hub.Service;

import java.util.Set;

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
public class GameResponse {

  private Long gameId;
  private String title;
  private Set<GenreResponse> genres;
  private String description;
  private String developer;
  private String publisher;
  private String releaseYear;
  private Double price;
  private byte [] gameCoverImage;


}
