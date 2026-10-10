package gamehub.game_Hub.ServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.PrivacySettingsRepository;
import gamehub.game_Hub.Repository.UserCommunitySettingsRepository;
import gamehub.game_Hub.Repository.UserStoreSettingsRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Request.OnboardRequest;
import gamehub.game_Hub.Request.Settings.UserSettingsUpdateRequests;
import gamehub.game_Hub.Service.SettingsService;
import gamehub.game_Hub.Service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {

  private final UserCommunitySettingsRepository userCommunitySettingsRepository;

  private final UserRepository userRepository;

  private final UserStoreSettingsRepository userStoreSettingsRepository;

  private final PrivacySettingsRepository userPrivacySettingsRepository;

  private final UserService userService;

  @Override
  @Transactional
  public Long updateUserSettings(final Authentication connectedUser,
      final UserSettingsUpdateRequests userSettingsUpdateRequests) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    userSettingsUpdateRequests.communitySettingsIds()
        .forEach(setting -> userCommunitySettingsRepository.findByUserAndId(user, setting.settingId())
            .orElseThrow(() -> new EntityNotFoundException(
                "No setting found with id: " + setting.settingId() + " for user " + user.getId()))
            .setAccess(setting.access()));

    userSettingsUpdateRequests.storeSettingsIds()
        .forEach(setting -> userStoreSettingsRepository.findByUserAndId(user, setting.settingId())
            .orElseThrow(() -> new EntityNotFoundException(
                "No setting found with id: " + setting.settingId() + " for user " + user.getId()))
            .setDisabled(setting.disabled()));

    userSettingsUpdateRequests.privacySettingsIds()
        .forEach(setting -> userPrivacySettingsRepository.findByUserAndId(user, setting.settingId())
            .orElseThrow(() -> new EntityNotFoundException(
                "No setting found with id: " + setting.settingId() + " for user " + user.getId()))
            .setAccess(setting.access()));

    return user.getId();
  }

  @Override
  public Long onboardUser(final Authentication connectedUser, OnboardRequest onboardRequest) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    user.setMainPlatform(onboardRequest.mainPlatform());
    user.setMicrophoneUsage(onboardRequest.microphoneUsage());
    userService.updateFavoriteGenres(onboardRequest.genreIds(), connectedUser);
    user.setUserOnboarded(true);
    userRepository.save(user);

    return user.getId();
  }

  @Override
  public Boolean isUserOnboarded(final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    return user.isUserOnboarded();
  }

}
