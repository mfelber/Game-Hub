package gamehub.game_Hub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Friendship;
import gamehub.game_Hub.Module.User.User;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

  Boolean existsByUser_IdAndFriend_Id(Long userId, Long friendId);

  List<Friendship> findByUser(User user);

  List<Friendship> findByUserOrderByFriend_LevelDesc(User user);

  List<Friendship> findTop5ByUserOrderByFriend_LevelDesc(User user);

}
