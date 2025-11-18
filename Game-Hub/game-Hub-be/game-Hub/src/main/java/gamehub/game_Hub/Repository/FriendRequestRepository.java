package gamehub.game_Hub.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.FriendRequest;
import gamehub.game_Hub.Module.User.User;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

  FriendRequest findBySender_IdAndReceiver_Id(Long senderId, Long receiverId);

  boolean existsBySender_IdAndReceiver_Id(Long senderId, Long receiverId);

  void deleteBySender_IdAndReceiver_Id(Long senderId, Long receiverId);

  boolean existsByReceiver_IdAndSender_Id(Long receiverId, Long senderId);


}
