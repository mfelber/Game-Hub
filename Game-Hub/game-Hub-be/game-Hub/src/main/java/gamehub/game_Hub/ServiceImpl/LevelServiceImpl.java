package gamehub.game_Hub.ServiceImpl;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Mapper.UserMapper;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.LevelProgressResponse;
import gamehub.game_Hub.Service.LevelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;

  @Override
  public LevelProgressResponse getUserLevelProgress(final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    return userMapper.toUserLevelProgress(user);
  }

}
