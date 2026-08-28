package gamehub.game_Hub.Response;

import gamehub.game_Hub.Module.CommunityGuidelineCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityGuidelinesResponse {

  private Long id;
  private String communityGuideline;
  private CommunityGuidelineCategoryResponse category;
  private String description;

}
