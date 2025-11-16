package gamehub.game_Hub.Mapper;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Response.LocationResponse;
import gamehub.game_Hub.Response.UserCommunityResponse;

@Service
public class CommunityMapper {

  public UserCommunityResponse toUserCommunityResponse(User user) {
    return UserCommunityResponse.builder()
        .userId(user.getId())
        .username(user.getName())
        .status(user.getStatus())
        .location(new LocationResponse(user.getLocation().name(), user.getLocation().getLocationIcon()))
        .userProfilePicture(FileUtils.readCoverFromLocation(user.getUserProfilePicture()))
        .profileColor(user.getProfileColor())
        .build();
  }

}
