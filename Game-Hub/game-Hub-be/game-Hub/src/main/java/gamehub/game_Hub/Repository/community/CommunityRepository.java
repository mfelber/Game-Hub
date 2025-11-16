package gamehub.game_Hub.Repository.community;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.User.User;

public interface CommunityRepository extends JpaRepository<User, Long> {

}
