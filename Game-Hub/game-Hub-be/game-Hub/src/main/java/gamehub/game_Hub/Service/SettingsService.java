package gamehub.game_Hub.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Request.OnboardRequest;
import gamehub.game_Hub.Request.Settings.UserSettingsUpdateRequests;

public interface SettingsService {

  Long updateUserSettings(Authentication connectedUser, UserSettingsUpdateRequests userSettingsUpdateRequests);

  Long onboardUser(Authentication connectedUser, OnboardRequest onboardRequest);

  Boolean isUserOnboarded(Authentication connectedUser);

}
