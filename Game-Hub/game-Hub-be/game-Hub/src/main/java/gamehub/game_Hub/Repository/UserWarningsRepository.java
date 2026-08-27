package gamehub.game_Hub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserWarnings;

public interface UserWarningsRepository extends JpaRepository<UserWarnings, Long> {

  List<UserWarnings> findByUserId(final Long user_id);

}
