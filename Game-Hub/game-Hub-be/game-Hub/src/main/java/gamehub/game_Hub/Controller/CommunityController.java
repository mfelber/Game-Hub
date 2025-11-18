package gamehub.game_Hub.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.FriendRequestRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.UserCommunityResponse;
import gamehub.game_Hub.Service.CommunityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("community")
@RequiredArgsConstructor
public class CommunityController {

  private final CommunityService communityService;

  private final UserRepository userRepository;

  private final FriendRequestRepository friendRequestRepository;

  @GetMapping("/get/all/users")
  public ResponseEntity<PageResponse<UserCommunityResponse>> findAllUsers(Authentication connectedUser,
      @RequestParam(defaultValue = "") String query,
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
    return ResponseEntity.ok(communityService.findAllUsers(connectedUser, query, page, size));
  }

  @PostMapping("/send/friend-request/{userId}")
  public ResponseEntity<Long> sendFriendRequest(Authentication connectedUser, @PathVariable Long userId) {
    return ResponseEntity.ok(communityService.sendFriendRequest(connectedUser, userId));
  }

  @PostMapping("/accept/friend-request/{userId}")
  public ResponseEntity<Long> acceptFriendRequest(Authentication connectedUser, @PathVariable Long userId) {
    return ResponseEntity.ok(communityService.acceptFriendRequest(connectedUser, userId));
  }

  @DeleteMapping("/cancel/friend-request/{userId}")
  public void cancelFriendRequest(Authentication connectedUser, @PathVariable Long userId) {
    communityService.cancelFriendRequest(connectedUser, userId);
  }

  @DeleteMapping("/delete/friend-request/{userId}")
  public void rejectFriendRequest(Authentication connectedUser, @PathVariable Long userId) {
    communityService.rejectFriendRequest(connectedUser, userId);
  }

  @GetMapping("/friend-request/status/sender/{userId}")
  public ResponseEntity<Boolean> friendRequestExistsFromSender(Authentication connectedUser,
      @PathVariable Long userId) {
    User authUser = (User) connectedUser.getPrincipal();
    User sender = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + authUser.getId()));
    boolean exists = friendRequestRepository.existsBySender_IdAndReceiver_Id(sender.getId(), userId);
    return ResponseEntity.ok(exists);
  }

  @GetMapping("/friend-request/status/receiver/{userId}")
  public ResponseEntity<Boolean> friendRequestExistsForReceiver(Authentication connectedUser,
      @PathVariable Long userId) {
    User authUser = (User) connectedUser.getPrincipal();
    User receiver = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + authUser.getId()));
    boolean exists = friendRequestRepository.existsByReceiver_IdAndSender_Id(receiver.getId(), userId);
    return ResponseEntity.ok(exists);
  }

  @GetMapping("/friends/{userId}/check")
  public ResponseEntity<Boolean> friendExistsForUser(Authentication connectedUser, @PathVariable Long userId) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + authUser.getId()));

    User friend = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

    boolean areFriends = userRepository.existsByIdAndFriends_Id(user.getId(), friend.getId());

    return ResponseEntity.ok(areFriends);

  }

}
