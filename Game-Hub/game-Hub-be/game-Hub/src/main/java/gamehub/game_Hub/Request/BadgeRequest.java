package gamehub.game_Hub.Request;

import gamehub.game_Hub.Module.BadgeCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BadgeRequest {

  private String name;

  private String description;

  private String iconPath;

  private BadgeCategory badgeCategory;

}
