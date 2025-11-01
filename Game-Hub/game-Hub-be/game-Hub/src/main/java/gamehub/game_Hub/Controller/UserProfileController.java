package gamehub.game_Hub.Controller;

import java.net.URI;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.UserPrivateResponse;
import gamehub.game_Hub.Response.UserPublicResponse;
import gamehub.game_Hub.Service.UserService;
import gamehub.game_Hub.Request.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("profile")
@RequiredArgsConstructor
public class UserProfileController {

  // location, bio, badge
  private final UserService userService;

  private final UserRepository userRepository;

  @PostMapping("/update-profile")
  public ResponseEntity<Long> updateUserProfile(
      final Authentication connectedUser,
      @RequestBody final UserUpdateRequest userUpdateRequest) {
    return ResponseEntity.ok(userService.updateUserProfile(connectedUser, userUpdateRequest));
  }

  @PostMapping("/update-bio")
  public ResponseEntity<Long> updateBio(final Authentication connectedUser,
      @RequestBody final UserUpdateRequest userUpdateRequest) {
  return ResponseEntity.ok(userService.updateBio(connectedUser, userUpdateRequest));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<UserPublicResponse> getUserPublic(@PathVariable final Long userId, final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new EntityNotFoundException("User with id " + authUser.getId() + " not found"));

    if (authUser.getId().equals(userId)) {
      URI redirectUri = URI.create("/gamehub/user/me");
      System.out.println("user is trying to view his own profile as public");
      return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
    }

    System.out.println("user is not trying to view his own profile as public");
    UserPublicResponse userPublicResponse = userService.getPublicProfile(userId, connectedUser);
    return ResponseEntity.ok(userPublicResponse);
  }

  @GetMapping("/user/me")
  public ResponseEntity<UserPrivateResponse> getUserPrivate(final Authentication connectedUser) {
    UserPrivateResponse userPrivateResponse = userService.getPrivateProfile(connectedUser);
    return ResponseEntity.ok(userPrivateResponse);
  }

  @PostMapping("/add/genres")
  public ResponseEntity<Void> updateFavoriteGenres(@RequestBody Set<Long> genreIds,
      final Authentication connectedUser) {
    userService.updateFavoriteGenres(genreIds,connectedUser);
    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/image", consumes = "multipart/form-data")
  public ResponseEntity<?> uploadProfileImage(
      @Parameter()
      @RequestPart("file") MultipartFile file,
      final Authentication connectedUser
  ){
    userService.uploadProfilePictureImage(connectedUser,file);
    return ResponseEntity.accepted().build();
  }


//   TODO upload banner image,


}
