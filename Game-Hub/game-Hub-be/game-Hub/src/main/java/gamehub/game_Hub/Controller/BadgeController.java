package gamehub.game_Hub.Controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Request.BadgeCategoryRequest;
import gamehub.game_Hub.Request.BadgeRequest;
import gamehub.game_Hub.Response.BadgeResponse;
import gamehub.game_Hub.Service.BadgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("badge")
@RequiredArgsConstructor
public class BadgeController {

  private final BadgeService badgeService;

  @PostMapping("/add-badge")
  public ResponseEntity<Long> addBadge(@Valid @RequestBody BadgeRequest badgeRequest) {
    return ResponseEntity.ok(badgeService.saveBadge(badgeRequest));
  }

  @PostMapping("/add-category")
  public ResponseEntity<Long> addCategory(@Valid @RequestBody BadgeCategoryRequest badgeCategoryRequest) {
    return ResponseEntity.ok(badgeService.saveCategory(badgeCategoryRequest));
  }

  @GetMapping("/all-badges")
  public ResponseEntity<List<BadgeResponse>> getAllUserBadges(final Authentication connectedUser) {
    return ResponseEntity.ok(badgeService.getAllUserBadges(connectedUser));
  }

}
