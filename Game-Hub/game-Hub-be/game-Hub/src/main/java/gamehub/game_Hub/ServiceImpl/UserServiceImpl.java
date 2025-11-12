package gamehub.game_Hub.ServiceImpl;

import static gamehub.game_Hub.Module.User.Status.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import gamehub.game_Hub.File.FileStorageService;
import gamehub.game_Hub.Module.Genre;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.genre.GenreRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Mapper.UserMapper;
import gamehub.game_Hub.Request.BannerRequest;
import gamehub.game_Hub.Response.StatusResponse;
import gamehub.game_Hub.Response.UserPrivateResponse;
import gamehub.game_Hub.Response.UserPublicResponse;
import gamehub.game_Hub.Service.UserService;
import gamehub.game_Hub.Request.UserUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;

  private final UserRepository userRepository;

  private final GenreRepository genreRepository;

  private final FileStorageService fileStorageService;

  @Override
  public Long updateUserProfile(final Authentication connectedUser,
      final UserUpdateRequest userUpdateRequest) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));
    // TODO try this
    // user = userMapper.toUser(userUpdateRequest);

    user = user.toBuilder()
        .firstName(userUpdateRequest.getFirstName())
        .lastName(userUpdateRequest.getLastName())
        .username(userUpdateRequest.getUsername())
        .email(userUpdateRequest.getEmail())
        .location(userUpdateRequest.getLocation())
        .build();

    return userRepository.save(user).getId();
  }

  @Transactional
  public UserPublicResponse getPublicProfile(final Long userId, final Authentication connectedUser) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));
    return userMapper.toUserPublicResponse(user);
  }

  @Override
  public UserPrivateResponse getPrivateProfile(final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));
    return userMapper.toUserPrivateResponse(user);
  }

  @Override
  public UserPrivateResponse getPrivateProfileShort(final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));
    return userMapper.toUserPrivateResponseShort(user);
  }

  @Override
  public UserPrivateResponse getBio(final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));
    return userMapper.toUserBioResponse(user);
  }

  @Override
  public Long setPredefinedBanner(BannerRequest bannerRequest, final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    // user = user.toBuilder().predefinedBanner(bannerId).banner(null).build();
    user = user.toBuilder().banner(bannerRequest.getBannerPath()).bannerType("PREDEFINED").build();
    return userRepository.save(user).getId();
  }

  @Override
  public StatusResponse getUserStatus(final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));
    return userMapper.toUserStatusResponse(user);
  }

  @Override
  public void uploadProfilePictureImage(final Authentication connectedUser,
      final MultipartFile file) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    var profilePicture = fileStorageService.saveUserImages(file, user.getId());
    user.setUserProfilePicture(profilePicture);
    userRepository.save(user);
  }

  @Override
  public void uploadBannerImage(final Authentication connectedUser, final MultipartFile file) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    var bannerImage = fileStorageService.saveUserImages(file, user.getId());
    // user.setPredefinedBanner(null);
    user.setBanner(bannerImage);
    user.setBannerType("CUSTOM");
    userRepository.save(user);
  }

  @Override
  public void setStatusToOnline(final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    user.setStatus(ONLINE);
    userRepository.save(user);
  }

  @Override
  public void setStatusToOffline(final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    user.setStatus(OFFLINE);
    userRepository.save(user);
  }

  @Override
  public void setStatusToAway(final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    user.setStatus(AWAY);
    userRepository.save(user);
  }


  @Transactional
  public void updateFavoriteGenres(final Set<Long> genresIds, final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Set<Genre> genres = genresIds.stream()
        .map(id -> genreRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("No genre found with id: " + id)))
        .collect(
            Collectors.toSet());

    user.setFavoriteGenres(genres);
    userRepository.save(user);
  }

  @Override
  public Long updateBio(final Authentication connectedUser, final UserUpdateRequest userUpdateRequest) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    user = user.toBuilder()
        .bio(userUpdateRequest.getBio())
        .build();

    return userRepository.save(user).getId();
  }

}
