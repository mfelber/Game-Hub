package gamehub.game_Hub.ServiceImpl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.CommunityMapper;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.UserCommunityResponse;
import gamehub.game_Hub.Service.CommunityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

  private final UserRepository userRepository;

  private final CommunityMapper communityMapper;

  @Override
  public PageResponse<UserCommunityResponse> findAllUsers(final Authentication connectedUser,String query, final int page,
      final int size) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    if (query.isEmpty()) {
      Page<User> users = userRepository.findAllByEmailIsNot(user.getEmail(), pageable);

      List<UserCommunityResponse> communityResponse = users.stream()
          .map(communityMapper::toUserCommunityResponse)
          .toList();

      return new PageResponse<>(
          communityResponse,
          users.getNumber(),
          users.getSize(),
          users.getTotalElements(),
          users.getTotalPages(),
          users.isFirst(),
          users.isLast()
      );
    } else {
      Page<User> users = userRepository.findAllByEmailIsNotAndUsername(user.getEmail(),query ,pageable);

      List<UserCommunityResponse> communityResponse = users.stream()
          .map(communityMapper::toUserCommunityResponse)
          .toList();

      return new PageResponse<>(
          communityResponse,
          users.getNumber(),
          users.getSize(),
          users.getTotalElements(),
          users.getTotalPages(),
          users.isFirst(),
          users.isLast()
      );
    }
  }


}
