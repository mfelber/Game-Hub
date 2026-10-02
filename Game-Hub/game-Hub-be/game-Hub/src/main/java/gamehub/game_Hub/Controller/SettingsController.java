package gamehub.game_Hub.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Request.Settings.UserSettingsUpdateRequests;
import gamehub.game_Hub.Service.SettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("settings")
@RequiredArgsConstructor
public class SettingsController {

  private final SettingsService settingsService;

  @PatchMapping("/update/user-settings")
  public ResponseEntity<Long> updateUserSettings(Authentication connectedUser,
      @Valid @RequestBody UserSettingsUpdateRequests userSettingsUpdateRequests) {
    return ResponseEntity.ok(settingsService.updateUserSettings(connectedUser, userSettingsUpdateRequests));
  }

}
