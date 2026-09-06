package gamehub.game_Hub.Repository;

import java.util.Set;

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

  Page<FriendRequest> findAllByReceiver_Id(Long receiverId, Pageable pageable);

  int findAllByReceiver_Id(Long receiverId);

  int countByReceiver_Id(Long receiverId);

  Set<Long> findReceiverIdsBySender_Id(Long senderId);

  Set<Long> findSenderIdsByReceiver_Id(Long senderId);

}
