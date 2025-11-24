package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Flags.UserCommunityFlag;

public interface UserCommunityFlagRepository extends JpaRepository<UserCommunityFlag, Long> {

}
