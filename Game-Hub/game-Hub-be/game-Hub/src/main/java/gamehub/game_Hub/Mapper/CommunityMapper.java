package gamehub.game_Hub.Mapper;

import java.util.Set;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.FriendRequest;
import gamehub.game_Hub.Module.Friendship;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.FriendRequestRepository;
import gamehub.game_Hub.Repository.FriendshipRepository;
import gamehub.game_Hub.Response.FriendRequestResponse;
import gamehub.game_Hub.Response.LevelResponse;
import gamehub.game_Hub.Response.LocationResponse;
import gamehub.game_Hub.Response.UserCommunityResponse;

@Service
public class CommunityMapper {

  private final FriendshipRepository friendshipRepository;

  private final FriendRequestRepository friendRequestRepository;

  public CommunityMapper(final FriendshipRepository friendshipRepository,
      final FriendRequestRepository friendRequestRepository) {
    this.friendshipRepository = friendshipRepository;
    this.friendRequestRepository = friendRequestRepository;
  }

  public UserCommunityResponse toUserCommunityResponse(User foundUser, User connetedUser, Set<Long> friendIds) {

    String joinedDate = foundUser.getCreatedAt().getMonth().name().charAt(0) + foundUser.getCreatedAt()
        .getMonth()
        .name()
        .substring(1)
        .toLowerCase() + " " + foundUser.getCreatedAt().getYear();

    Boolean isFriend = friendIds.contains(foundUser.getId());

    Boolean friendReqSent = friendRequestRepository.existsBySender_IdAndReceiver_Id(connetedUser.getId(),
        foundUser.getId());

    Boolean friendReqReceived = friendRequestRepository.existsBySender_IdAndReceiver_Id(foundUser.getId(),
        connetedUser.getId());

    return UserCommunityResponse.builder()
        .userId(foundUser.getId())
        .username(foundUser.getName())
        .joinedDate(joinedDate)
        .status(foundUser.getStatus())
        .libraryCount(foundUser.getLibrary().size())
        .friendsCount(foundUser.getFriends().size())
        .reviewsCount(0)
        .location(new LocationResponse(foundUser.getLocation().name(), foundUser.getLocation().getLocationIcon()))
        .userProfilePicture(FileUtils.readCoverFromLocation(foundUser.getUserProfilePicture()))
        .profileColor(foundUser.getProfileColor())
        .isFriend(isFriend)
        .friendRequestSent(friendReqSent)
        .friendRequestReceived(friendReqReceived)
        .build();
  }

  public FriendRequestResponse toFriendRequestResponse(FriendRequest friendRequest) {

    String requestSentAt =
        friendRequest.getCreatedAt().getDayOfMonth() + " " +
            friendRequest.getCreatedAt().getMonth().name() + " " + friendRequest.getCreatedAt().getYear();

    User sender = friendRequest.getSender();

    return FriendRequestResponse.builder()
        .userId(sender.getId())
        .username(sender.getName())
        .status(sender.getStatus())
        .level(new LevelResponse(sender.getLevel().getId(), sender.getLevel().getLevelNumber(),
            sender.getLevel().getLevelColor()))
        .location(new LocationResponse(sender.getLocation().name(), sender.getLocation().getLocationIcon()))
        .userProfilePicture(FileUtils.readCoverFromLocation(sender.getUserProfilePicture()))
        .profileColor(sender.getProfileColor())
        .createdAt(requestSentAt)
        .build();
  }

}
