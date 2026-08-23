package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.UserSuspensions;

public interface UserSuspensionRepository extends JpaRepository<UserSuspensions, Long> {

}
