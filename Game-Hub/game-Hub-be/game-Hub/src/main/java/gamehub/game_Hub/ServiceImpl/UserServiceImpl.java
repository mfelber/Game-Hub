package gamehub.game_Hub.ServiceImpl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gamehub.game_Hub.Module.Genre;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.genre.GenreRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Mapper.UserMapper;
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
  
  @Override
  public Long updateUserProfile(final Authentication connectedUser,
      final UserUpdateRequest userUpdateRequest) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    user = user.toBuilder()
        .firstName(userUpdateRequest.getFirstName())
        .lastName(userUpdateRequest.getLastName())
        .username(userUpdateRequest.getUsername())
        .email(userUpdateRequest.getEmail())
        .userProfilePicture(user.getUserProfilePicture())
        .location(userUpdateRequest.getLocation())
        .build();

    return userRepository.save(user).getId();
  }

  @Transactional
  public UserPublicResponse getPublicProfile(final Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));

    return userMapper.toUserPublicResponse(user);
  }

  @Transactional
  public void updateFavoriteGenres(final Set<Long> genresIds, final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Set<Genre> genres = genresIds.stream().map(id -> genreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No genre found with id: " + id))).collect(
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
