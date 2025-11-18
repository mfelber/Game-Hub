package gamehub.game_Hub.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.CommunityMapper;
import gamehub.game_Hub.Module.FriendRequest;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.FriendRequestRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.FriendRequestResponse;
import gamehub.game_Hub.Response.UserCommunityResponse;
import gamehub.game_Hub.Service.CommunityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

  private final UserRepository userRepository;

  private final CommunityMapper communityMapper;

  private final FriendRequestRepository friendRequestRepository;

  @Override
  public PageResponse<UserCommunityResponse> findAllUsers(final Authentication connectedUser, String query,
      final int page,
      final int size) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    if (query.isEmpty()) {
      Page<User> users = userRepository.findAllByEmailIsNot(user.getEmail(), pageable);

      List<UserCommunityResponse> communityResponse = users.stream()
          .map(communityMapper::toUserCommunityResponse)
          .toList();

      return new PageResponse<>(
          communityResponse,
          users.getNumber(),
          users.getSize(),
          users.getTotalElements(),
          users.getTotalPages(),
          users.isFirst(),
          users.isLast()
      );
    } else {
      Page<User> users = userRepository.findAllByEmailIsNotAndUsername(user.getEmail(), query, pageable);

      List<UserCommunityResponse> communityResponse = users.stream()
          .map(communityMapper::toUserCommunityResponse)
          .toList();

      return new PageResponse<>(
          communityResponse,
          users.getNumber(),
          users.getSize(),
          users.getTotalElements(),
          users.getTotalPages(),
          users.isFirst(),
          users.isLast()
      );
    }
  }

  @Override
  public Long sendFriendRequest(final Authentication connectedUser, final Long userId) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    if (userId.equals(user.getId())) {
      throw new IllegalArgumentException("Cannot send friend request to yourself");
    }

    boolean exists = friendRequestRepository.existsBySender_IdAndReceiver_Id(user.getId(), userId);

    if (exists) {
      throw new IllegalStateException("Friend request already send");
    }

    User receiver = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("Receiver not found with id: " + userId));

    FriendRequest friendRequest = new FriendRequest();
    friendRequest.setSender(user);
    friendRequest.setReceiver(receiver);
    friendRequest.setCreatedAt(LocalDateTime.now());

    friendRequestRepository.save(friendRequest);
    return friendRequest.getId();
  }

  @Override
  @Transactional
  public void cancelFriendRequest(final Authentication connectedUser, final Long userId) {
    User authUser = (User) connectedUser.getPrincipal();
    User sender = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    User receiver = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("Receiver not found with id: " + userId));

    boolean exists = friendRequestRepository.existsBySender_IdAndReceiver_Id(sender.getId(), userId);

    if (!exists) {
      throw new IllegalStateException("No friend request to cancel");
    }

    friendRequestRepository.deleteBySender_IdAndReceiver_Id(sender.getId(), receiver.getId());
  }

  @Override
  @Transactional
  public void rejectFriendRequest(final Authentication connectedUser, final Long userId) {
    User authUser = (User) connectedUser.getPrincipal();
    User receiver = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    User sender = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("Receiver not found with id: " + userId));

    friendRequestRepository.deleteBySender_IdAndReceiver_Id(sender.getId(), receiver.getId());
  }

  @Transactional
  public Long acceptFriendRequest(final Authentication connectedUser, final Long userId) {
    User authUser = (User) connectedUser.getPrincipal();
    User receiver = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    User sender = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("Receiver not found with id: " + userId));

    System.out.println("receiver" + receiver.getId());
    System.out.println("sender" + sender.getId());

    receiver.addFriend(sender);

    userRepository.save(receiver);

    friendRequestRepository.deleteBySender_IdAndReceiver_Id(sender.getId(), receiver.getId());

    return receiver.getId();
  }

  @Override
  public PageResponse<FriendRequestResponse> getAllMyFriendRequests(final Authentication connectedUser,
      final int page,
      final int size) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Pageable pageable = PageRequest.of(page, size,Sort.by("createdAt").descending());

    Page<FriendRequest> requests = friendRequestRepository.findAllByReceiver_Id(user.getId(),pageable);

    List<FriendRequestResponse> friendRequests = requests.stream().map(communityMapper::toFriendRequestResponse).toList();

    return new PageResponse<>(
        friendRequests,
        requests.getNumber(),
        requests.getSize(),
        requests.getTotalElements(),
        requests.getTotalPages(),
        requests.isFirst(),
        requests.isLast()
    );
  }



}
