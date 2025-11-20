package gamehub.game_Hub.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Response.LevelProgressResponse;
import gamehub.game_Hub.Service.LevelService;

@RestController
@RequestMapping("user-level")
public class UserLevelController {

  private final LevelService levelService;

  public UserLevelController(final LevelService levelService) {
    this.levelService = levelService;
  }

  @GetMapping("/user/level-progress")
  public ResponseEntity<LevelProgressResponse> getUserLevelProgress(Authentication connectedUser) {
    return ResponseEntity.ok(levelService.getUserLevelProgress(connectedUser));
  }

}
