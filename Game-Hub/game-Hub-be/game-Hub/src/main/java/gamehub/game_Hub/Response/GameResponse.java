package gamehub.game_Hub.Response;

import java.util.List;
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
  private List<GenreResponse> genres;
  private String description;
  private String developer;
  private String publisher;
  private String releaseYear;
  private Double price;
  private Double discountPrice;
  private Integer discountPercent;
  private boolean hasDiscount;
  private boolean isInCart;
  private boolean isInLibrary;
  private boolean isInWishList;
  private AgeRatingResponse ageRating;
  private byte [] gameCoverImage;
  private SystemRequirements systemRequirements;
  private Set<PlatformResponse> platforms;
  private Set<LanguageResponse> languages;
  private Set<SubtitleResponse> subtitles;

}
