package gamehub.game_Hub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.userSettings.PrivacySettings;

public interface PrivacySettingsRepository extends JpaRepository<PrivacySettings, Long> {

  List<PrivacySettings> findByUser(User user);

}
