package gamehub.game_Hub.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.userSettings.StoreSettings;

public interface UserStoreSettingsRepository extends JpaRepository<StoreSettings, Long> {

  List<StoreSettings> findByUser(User user);

  Optional<StoreSettings> findByUserAndId(User user, Long id);

}
