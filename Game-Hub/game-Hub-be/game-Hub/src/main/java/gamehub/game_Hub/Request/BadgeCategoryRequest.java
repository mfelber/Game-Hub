package gamehub.game_Hub.Request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BadgeCategoryRequest {

  private String categoryName;

  private String categoryNameCode;



}
