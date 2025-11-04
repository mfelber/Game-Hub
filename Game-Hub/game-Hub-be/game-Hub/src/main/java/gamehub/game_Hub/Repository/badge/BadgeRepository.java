package gamehub.game_Hub.Repository.badge;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Badge;
import gamehub.game_Hub.Module.User.User;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

  // List<Badge> findByUsersContaining(User connectedUser);

  // Badge findByName(String gameCollectorLevel1);

  Optional<Badge> findByName(String name);

  List<Badge> findByOwnedUsers(User ownedUsers);

}
