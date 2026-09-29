package gamehub.game_Hub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.userSettings.CommunitySettings;
import gamehub.game_Hub.Response.CommunitySettingsResponse;

public interface UserCommunitySettingsRepository extends JpaRepository<CommunitySettings, Long> {

  List<CommunitySettings> findByUser(User user);

}
