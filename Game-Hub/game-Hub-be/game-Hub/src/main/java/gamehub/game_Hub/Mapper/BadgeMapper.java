package gamehub.game_Hub.Mapper;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.Badge;
import gamehub.game_Hub.Module.BadgeCategory;
import gamehub.game_Hub.Request.BadgeCategoryRequest;
import gamehub.game_Hub.Request.BadgeRequest;
import gamehub.game_Hub.Response.BadgeResponse;

@Service
public class BadgeMapper {

  public Badge toBadge(BadgeRequest badgeRequest) {

    return Badge.builder()
        .name(badgeRequest.getName())
        .description(badgeRequest.getDescription())
        .badgeCategory(badgeRequest.getBadgeCategory())
        .iconPath(badgeRequest.getIconPath())
        .build();
  }

  public BadgeCategory toBadgeCategory(final BadgeCategoryRequest badgeCategoryRequest) {

    return BadgeCategory.builder()
        .categoryNameCode(badgeCategoryRequest.getCategoryNameCode())
        .categoryName(badgeCategoryRequest.getCategoryName())
        .build();
  }

  public BadgeResponse toBadgeResponse(Badge badge) {

    return BadgeResponse.builder()
        .id(badge.getId())
        .title(badge.getName())
        .description(badge.getDescription())
        .iconPath(badge.getIconPath())
        .build();
  }

}
