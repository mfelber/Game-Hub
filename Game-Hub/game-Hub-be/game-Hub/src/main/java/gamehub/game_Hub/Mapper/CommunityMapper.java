package gamehub.game_Hub.Mapper;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.FriendRequest;
import gamehub.game_Hub.Module.Friendship;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.FriendRequestRepository;
import gamehub.game_Hub.Response.FriendProfileResponse;
import gamehub.game_Hub.Response.FriendRequestResponse;
import gamehub.game_Hub.Response.LevelResponse;
import gamehub.game_Hub.Response.CountryResponse;
import gamehub.game_Hub.Response.UserCommunityResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityMapper {

  private final FriendRequestRepository friendRequestRepository;

  private final GameMapper gameMapper;

  public UserCommunityResponse toUserCommunityResponse(User foundUser, User connetedUser, Set<Long> friendIds) {

    String joinedDate = foundUser.getCreatedAt().getMonth().name().charAt(0) + foundUser.getCreatedAt()
        .getMonth()
        .name()
        .substring(1)
        .toLowerCase() + " " + foundUser.getCreatedAt().getYear();

    Boolean isFriend = friendIds.contains(foundUser.getId());

    Boolean friendReqSent = friendRequestRepository.existsBySender_IdAndReceiver_Id(connetedUser.getId(),
        foundUser.getId());

    Boolean friendReqReceived = friendRequestRepository.existsByReceiver_IdAndSender_Id(connetedUser.getId(),
        foundUser.getId());

    return UserCommunityResponse.builder()
        .userId(foundUser.getId())
        .username(foundUser.getName())
        .joinedDate(joinedDate)
        .status(foundUser.getStatus())
        .libraryCount(foundUser.getLibrary().size())
        .friendsCount(foundUser.getFriends().size())
        .reviewsCount(0)
        .country(new CountryResponse(foundUser.getCountry().name(), foundUser.getCountry().getCountryName(), foundUser.getCountry().getCountryIcon()))
        .userProfilePicture(FileUtils.readCoverFromLocation(foundUser.getUserProfilePicture()))
        .profileColor(foundUser.getProfileColor())
        .isFriend(isFriend)
        .friendRequestSent(friendReqSent)
        .friendRequestReceived(friendReqReceived)
        .currentlyPlaying(foundUser.getCurrentlyPlayingGame() != null ? gameMapper.toGameResponseShort(
            foundUser.getCurrentlyPlayingGame()) : null)
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
        .country(new CountryResponse(sender.getCountry().name(), sender.getCountry().getCountryName(), sender.getCountry().getCountryIcon()))
        .userProfilePicture(FileUtils.readCoverFromLocation(sender.getUserProfilePicture()))
        .profileColor(sender.getProfileColor())
        .createdAt(requestSentAt)
        .build();
  }

  public List<FriendProfileResponse> toUserFriendsResponse(final List<Friendship> friends) {

    return friends.stream().map(friendship -> FriendProfileResponse.builder()
        .userId(friendship.getFriend().getId())
        .userName(friendship.getFriend().getName())
        .level(new LevelResponse(friendship.getFriend().getLevel().getId(),
            friendship.getFriend().getLevel().getLevelNumber(), friendship.getFriend().getLevel().getLevelColor()))
        .status(friendship.getFriend().getStatus())
        .currentlyPlaying(friendship.getFriend().getCurrentlyPlayingGame() != null ? gameMapper.toGameResponseShort(
            friendship.getFriend().getCurrentlyPlayingGame()) : null)
        .userProfilePicture(FileUtils.readCoverFromLocation(friendship.getFriend().getUserProfilePicture()))
        .profileColor(friendship.getFriend().getProfileColor())
        .build()).toList();
  }

}
