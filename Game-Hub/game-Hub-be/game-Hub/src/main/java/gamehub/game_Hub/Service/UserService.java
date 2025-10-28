package gamehub.game_Hub.Service;

import java.util.Set;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Request.UserUpdateRequest;
import gamehub.game_Hub.Response.UserPublicResponse;

public interface UserService {

  Long updateUserProfile(Authentication connectedUser, UserUpdateRequest userUpdateRequest);

  UserPublicResponse getPublicProfile(Long userId);

  void updateFavoriteGenres(Set<Long> genreIds, Authentication connectedUser);

}
