package gamehub.game_Hub.Response;

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
public class GamePreviewResponse {
  private Long gameId;
  private String title;
  private byte[] gameCoverImage;
  private Double price;
  private Double discountPrice;
  private Integer discountPercent;
  private boolean hasDiscount;
  private Set<GenreResponse> genres;

}
