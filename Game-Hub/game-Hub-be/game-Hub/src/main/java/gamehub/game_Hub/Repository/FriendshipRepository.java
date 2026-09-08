package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

  Boolean existsByUser_IdAndFriend_Id(Long userId, Long friendId);

}
