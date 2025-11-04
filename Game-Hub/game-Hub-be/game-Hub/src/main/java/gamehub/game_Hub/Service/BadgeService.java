package gamehub.game_Hub.Service;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Request.BadgeCategoryRequest;
import gamehub.game_Hub.Request.BadgeRequest;
import gamehub.game_Hub.Response.BadgeResponse;
import jakarta.validation.Valid;

public interface BadgeService {

  Long saveBadge(@Valid BadgeRequest badgeRequest);

  Long saveCategory(@Valid BadgeCategoryRequest badgeCategoryRequest);

  List<BadgeResponse> getAllUserBadges(Authentication connectedUser);

  // List<BadgeResponse> getAllUserBadges(Authentication connectedUser);

}
