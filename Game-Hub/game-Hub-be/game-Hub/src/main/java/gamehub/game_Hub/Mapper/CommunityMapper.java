package gamehub.game_Hub.Mapper;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.FriendRequest;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Response.FriendRequestResponse;
import gamehub.game_Hub.Response.LevelResponse;
import gamehub.game_Hub.Response.LocationResponse;
import gamehub.game_Hub.Response.UserCommunityResponse;

@Service
public class CommunityMapper {

  public UserCommunityResponse toUserCommunityResponse(User user) {

    String joinedDate = user.getCreatedAt().getMonth().name().charAt(0) + user.getCreatedAt()
        .getMonth()
        .name()
        .substring(1)
        .toLowerCase() + " " + user.getCreatedAt().getYear();

    return UserCommunityResponse.builder()
        .userId(user.getId())
        .username(user.getName())
        .joinedDate(joinedDate)
        .status(user.getStatus())
        .libraryCount(user.getLibrary().size())
        .friendsCount(user.getFriends().size())
        .reviewsCount(0)
        .location(new LocationResponse(user.getLocation().name(), user.getLocation().getLocationIcon()))
        .userProfilePicture(FileUtils.readCoverFromLocation(user.getUserProfilePicture()))
        .profileColor(user.getProfileColor())
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
        .level(new LevelResponse(sender.getLevel().getId(), sender.getLevel().getLevelNumber(),sender.getLevel().getLevelColor()))
        .location(new LocationResponse(sender.getLocation().name(), sender.getLocation().getLocationIcon()))
        .userProfilePicture(FileUtils.readCoverFromLocation(sender.getUserProfilePicture()))
        .profileColor(sender.getProfileColor())
        .createdAt(requestSentAt)
        .build();
  }



}
