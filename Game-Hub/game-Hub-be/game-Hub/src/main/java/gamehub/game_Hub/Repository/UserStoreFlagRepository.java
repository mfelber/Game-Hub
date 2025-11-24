package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Flags.UserStoreFlag;

public interface UserStoreFlagRepository extends JpaRepository<UserStoreFlag, Long> {

}
