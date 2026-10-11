package gamehub.game_Hub.Service;

import java.util.List;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Response.FriendRequestResponse;
import gamehub.game_Hub.Response.MicrophoneUsageResponse;
import gamehub.game_Hub.Response.PlatformResponse;
import gamehub.game_Hub.Response.RegionResponse;
import gamehub.game_Hub.Response.UserCommunityResponse;
import gamehub.game_Hub.enums.Country;
import gamehub.game_Hub.enums.MicrophoneUsage;
import gamehub.game_Hub.enums.Platform;
import gamehub.game_Hub.enums.Region;

public interface CommunityService {

  PageResponse<UserCommunityResponse> findAllUsers(Authentication connectedUser, String query, List<Country> countries,
      List<Region> regions, List<Platform> platforms, List<MicrophoneUsage> voice, boolean onlyOnline, int page, int size);

  Long sendFriendRequest(Authentication connectedUser, Long userId);

  void cancelFriendRequest(Authentication connectedUser, Long userId);

  void rejectFriendRequest(Authentication connectedUser, Long userId);

  Long acceptFriendRequest(Authentication connectedUser, Long userId);

  PageResponse<FriendRequestResponse> getAllMyFriendRequests(Authentication connectedUser, int page, int size);

  List<PlatformResponse> getAllGamingPlatforms();

  List<RegionResponse> getAllRegions();

  List<MicrophoneUsageResponse> getAllMicrophoneUsages();

}
