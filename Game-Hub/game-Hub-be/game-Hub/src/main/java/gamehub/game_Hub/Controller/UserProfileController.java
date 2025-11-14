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
import gamehub.game_Hub.Request.BannerRequest;
import gamehub.game_Hub.Response.StatusResponse;
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

  private final UserService userService;

  private final UserRepository userRepository;

  // Allows a user to update their profile information: first name, last name, username, email, location, and card color
  @PostMapping("/update-profile")
  public ResponseEntity<Long> updateUserProfile(
      final Authentication connectedUser,
      @RequestBody final UserUpdateRequest userUpdateRequest) {
    return ResponseEntity.ok(userService.updateUserProfile(connectedUser, userUpdateRequest));
  }

  // Allows a user to upload profile picture
  @PostMapping(value = "/image", consumes = "multipart/form-data")
  public ResponseEntity<?> uploadProfileImage(
      @Parameter()
      @RequestPart("file") MultipartFile file,
      final Authentication connectedUser
  ){
    userService.uploadProfilePictureImage(connectedUser,file);
    return ResponseEntity.accepted().build();
  }

  // Allows a user to upload custom banner image
  @PostMapping(value = "/custom/banner", consumes = "multipart/form-data")
  public ResponseEntity<?> uploadBannerImage(
      @Parameter()
      @RequestPart("file") MultipartFile file,
      final Authentication connectedUser
  ) {
    userService.uploadBannerImage(connectedUser,file);
    return ResponseEntity.accepted().build();
  }

  // Allows a user to set a banner from predefined banners
  @PostMapping("/predefined/banner")
  public ResponseEntity<?> setPredefinedBanner(@RequestBody BannerRequest bannerRequest, final Authentication connectedUser) {

    userService.setPredefinedBanner(bannerRequest, connectedUser);
    return ResponseEntity.accepted().build();
  }

  // Allows a user to update their bio
  @PostMapping("/update-bio")
  public ResponseEntity<Long> updateBio(final Authentication connectedUser,
      @RequestBody final UserUpdateRequest userUpdateRequest) {
  return ResponseEntity.ok(userService.updateBio(connectedUser, userUpdateRequest));
  }

  // Allows a user to update their favorite genres
  @PostMapping("/add/favorite/genres")
  public ResponseEntity<Void> updateFavoriteGenres(@RequestBody Set<Long> genreIds,
      final Authentication connectedUser) {
    userService.updateFavoriteGenres(genreIds,connectedUser);
    return ResponseEntity.ok().build();
  }

  // Retrieve the private profile information of the currently authenticated user
  @GetMapping("/user/me")
  public ResponseEntity<UserPrivateResponse> getUserPrivate(final Authentication connectedUser) {
    UserPrivateResponse userPrivateResponse = userService.getPrivateProfile(connectedUser);
    return ResponseEntity.ok(userPrivateResponse);
  }

  // Retrieve the private profile information of the currently authenticated user for menu
  @GetMapping("/user/me-short")
  public ResponseEntity<UserPrivateResponse> getUserPrivateShort(final Authentication connectedUser) {
    UserPrivateResponse userPrivateResponse = userService.getPrivateProfileShort(connectedUser);
    return ResponseEntity.ok(userPrivateResponse);
  }

  // Retrieve another user's profile information
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

  // Get user bio
  @GetMapping("/bio")
  public ResponseEntity<UserPrivateResponse> getBio(final Authentication connectedUser) {
    UserPrivateResponse userPrivateResponse = userService.getBio(connectedUser);
    return ResponseEntity.ok(userPrivateResponse);
  }

  // Retrieve the current status of a user: ONLINE, AWAY, or OFFLINE
  @GetMapping("/status")
  public ResponseEntity<StatusResponse> getUserStatus(final Authentication connectedUser) {
    StatusResponse statusResponse = userService.getUserStatus(connectedUser);
    return ResponseEntity.ok(statusResponse);
  }

  // Set the user's status to ONLINE
  @PostMapping("/status/online")
  public ResponseEntity<Void> setStatusToOnline(final Authentication connectedUser) {
    userService.setStatusToOnline(connectedUser);
    return ResponseEntity.accepted().build();
  }

  // Set the user's status to OFFLINE
  @PostMapping("/status/offline")
  public ResponseEntity<Void> setStatusToOffline(final Authentication connectedUser) {
    userService.setStatusToOffline(connectedUser);
    return ResponseEntity.accepted().build();
  }

  // Set the user's status to AWAY
  @PostMapping("/status/away")
  public ResponseEntity<Void> setStatusToAway(final Authentication connectedUser) {
    userService.setStatusToAway(connectedUser);
    return ResponseEntity.accepted().build();
  }

}