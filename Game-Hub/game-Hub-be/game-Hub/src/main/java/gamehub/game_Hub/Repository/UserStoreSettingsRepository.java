package gamehub.game_Hub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.userSettings.StoreSettings;

public interface UserStoreSettingsRepository extends JpaRepository<StoreSettings, Long> {

  List<StoreSettings> findByUser(User user);

}
