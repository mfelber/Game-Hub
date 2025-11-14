package gamehub.game_Hub.Service;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import gamehub.game_Hub.Request.BannerRequest;
import gamehub.game_Hub.Request.UserUpdateRequest;
import gamehub.game_Hub.Response.StatusResponse;
import gamehub.game_Hub.Response.UserPrivateResponse;
import gamehub.game_Hub.Response.UserPublicResponse;

public interface UserService {

  Long updateUserProfile(Authentication connectedUser, UserUpdateRequest userUpdateRequest);

  UserPublicResponse getPublicProfile(Long userId, Authentication connectedUser);

  void updateFavoriteGenres(Set<Long> genreIds, Authentication connectedUser);

  Long updateBio(Authentication connectedUser, UserUpdateRequest userUpdateRequest);

  UserPrivateResponse getPrivateProfile(Authentication connectedUser);

  void uploadProfilePictureImage(Authentication connectedUser, MultipartFile file);

  void uploadBannerImage(Authentication connectedUser, MultipartFile file);

  void setStatusToOnline(Authentication connectedUser);

  void setStatusToOffline(Authentication connectedUser);

  void setStatusToAway(Authentication connectedUser);

  StatusResponse getUserStatus(Authentication connectedUser);

  UserPrivateResponse getPrivateProfileShort(Authentication connectedUser);

  UserPrivateResponse getBio(Authentication connectedUser);

  Long setPredefinedBanner(BannerRequest bannerRequest, Authentication connectedUser);

}
