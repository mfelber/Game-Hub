package gamehub.game_Hub.Controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Response.UserPublicResponse;
import gamehub.game_Hub.Service.UserService;
import gamehub.game_Hub.Request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("profile")
@RequiredArgsConstructor
public class UserProfileController {

  // location, bio, badge
  private final UserService userService;

  @PostMapping("/update")
  public ResponseEntity<Long> updateLocation(
      final Authentication connectedUser,
      @RequestBody final UserUpdateRequest userUpdateRequest) {
    return ResponseEntity.ok(userService.updateUserProfile(connectedUser, userUpdateRequest));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<UserPublicResponse> getUser(@PathVariable final Long userId) {
    UserPublicResponse userPublicResponse = userService.getPublicProfile(userId);
    return ResponseEntity.ok(userPublicResponse);
  }

  @PostMapping("/add/genres")
  public ResponseEntity<Void> updateFavoriteGenres(@RequestBody Set<Long> genreIds,
      final Authentication connectedUser) {
    userService.updateFavoriteGenres(genreIds,connectedUser);
    return ResponseEntity.ok().build();

  }


}
