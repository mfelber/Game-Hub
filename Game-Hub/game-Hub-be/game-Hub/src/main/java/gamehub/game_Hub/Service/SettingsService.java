package gamehub.game_Hub.Service;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Request.Settings.UserSettingsUpdateRequests;

public interface SettingsService {

  Long updateUserSettings(Authentication connectedUser, UserSettingsUpdateRequests userSettingsUpdateRequests);

}
