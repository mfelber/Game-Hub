package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Flags.StoreFlagType;
import gamehub.game_Hub.Module.Flags.UserStoreFlag;

public interface UserStoreFlagRepository extends JpaRepository<UserStoreFlag, Long> {

  boolean existsByUser_IdAndUserFlagType_FlagCodeAndValueTrue(Long userId, String userFlagTypeFlagCode);

}
