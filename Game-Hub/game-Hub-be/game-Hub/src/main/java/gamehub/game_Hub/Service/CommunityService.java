package gamehub.game_Hub.Service;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Response.UserCommunityResponse;

public interface CommunityService {

  PageResponse<UserCommunityResponse> findAllUsers(Authentication connectedUser,String query, int page, int size);

  Long sendFriendRequest(Authentication connectedUser, Long userId);

  void cancelFriendRequest(Authentication connectedUser, Long userId);

}
