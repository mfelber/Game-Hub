package gamehub.game_Hub.ServiceImpl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gamehub.game_Hub.Module.User.User;
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

  @Override
  public Long updateUserProfile(final Authentication connectedUser,
      final UserUpdateRequest userUpdateRequest) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));
    return userRepository.save(user).getId();
  }

  @Transactional
  public UserPublicResponse getPublicProfile(final Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));

    return userMapper.toUserPublicResponse(user);
  }

}
