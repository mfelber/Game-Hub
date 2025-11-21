package gamehub.game_Hub.Response;

import java.util.Set;

import gamehub.game_Hub.Module.SystemRequirements;
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
  private AgeratingResponse ageRating;
  private byte [] gameCoverImage;
  private SystemRequirements systemRequirements;
  private Set<PlatformResponse> platforms;
  private Set<LanguageResponse> languages;
  private Set<SubtitleResponse> subtitles;

}
