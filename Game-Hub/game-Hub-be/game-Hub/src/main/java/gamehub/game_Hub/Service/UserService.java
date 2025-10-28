package gamehub.game_Hub.Service;

import org.springframework.security.core.Authentication;

public interface UserService {

  Long updateUserProfile(Authentication connectedUser, UserUpdateRequest userUpdateRequest);

  UserPublicResponse getPublicProfile(Long userId);

}
