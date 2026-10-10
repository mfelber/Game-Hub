package gamehub.game_Hub.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Request.OnboardRequest;
import gamehub.game_Hub.Request.Settings.UserSettingsUpdateRequests;
import gamehub.game_Hub.Response.UserSettingsResponse;
import gamehub.game_Hub.Service.SettingsService;
import gamehub.game_Hub.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("settings")
@RequiredArgsConstructor
public class SettingsController {

  private final SettingsService settingsService;

  private final UserService userService;

  @PatchMapping("/update/user-settings")
  public ResponseEntity<Long> updateUserSettings(Authentication connectedUser,
      @Valid @RequestBody UserSettingsUpdateRequests userSettingsUpdateRequests) {
    return ResponseEntity.ok(settingsService.updateUserSettings(connectedUser, userSettingsUpdateRequests));
  }

  @GetMapping("/settings")
  public UserSettingsResponse getUserSettings(Authentication connectedUser) {
    return userService.getUserSettings(connectedUser);
  }

  @PatchMapping("/onboarding")
  public ResponseEntity<Long> onboardUser(Authentication connectedUser,
      @Valid @RequestBody OnboardRequest onboardRequest) {
    return ResponseEntity.ok(settingsService.onboardUser(connectedUser, onboardRequest));
  }

  @GetMapping("/onboarded")
  public ResponseEntity<Boolean> isUserOnboarded(Authentication connectedUser) {
    return ResponseEntity.ok(settingsService.isUserOnboarded(connectedUser));
  }

}
