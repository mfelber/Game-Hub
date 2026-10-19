package gamehub.game_Hub.ServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.CommunityMapper;
import gamehub.game_Hub.Module.FriendRequest;
import gamehub.game_Hub.Module.Friendship;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.FriendRequestRepository;
import gamehub.game_Hub.Repository.FriendshipRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.FriendRequestResponse;
import gamehub.game_Hub.Response.MicrophoneUsageResponse;
import gamehub.game_Hub.Response.PlatformResponse;
import gamehub.game_Hub.Response.RegionResponse;
import gamehub.game_Hub.Response.UserCommunityResponse;
import gamehub.game_Hub.Service.CommunityService;
import gamehub.game_Hub.enums.AccountStatus;
import gamehub.game_Hub.enums.Country;
import gamehub.game_Hub.enums.MicrophoneUsage;
import gamehub.game_Hub.enums.Platform;
import gamehub.game_Hub.enums.Region;
import gamehub.game_Hub.enums.Role;
import gamehub.game_Hub.specification.UserSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

  private final UserRepository userRepository;

  private final CommunityMapper communityMapper;

  private final FriendRequestRepository friendRequestRepository;

  private static final List<AccountStatus> FINDABLE_STATUSES = List.of(
      AccountStatus.ACTIVE, AccountStatus.SUSPENDED
  );

  private final FriendshipRepository friendshipRepository;

  @Override
  public PageResponse<UserCommunityResponse> findAllUsers(final Authentication connectedUser, String query,
      List<Country> countries,
      List<Region> regions,
      List<Platform> platforms,
      List<MicrophoneUsage> voice,
      boolean onlyOnline,
      final int page,
      final int size) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    Set<Long> friendIds = user.getFriends().stream().map(friendship -> friendship.getFriend().getId()).collect(
        Collectors.toSet());

    Specification<User> spec = Specification.allOf(UserSpecification.emailIsNot(user.getEmail()),
        UserSpecification.hasRole(Role.USER),
        UserSpecification.accountStatusIn(FINDABLE_STATUSES));

    if (query != null && query.isBlank()) {
      spec = spec.and(UserSpecification.usernameContains(query));
    }

    if (countries != null && !countries.isEmpty()) {
      spec = spec.and(UserSpecification.countryIn(countries));
    } else if (regions != null && !regions.isEmpty()) {
      spec = spec.and(UserSpecification.regionIn(regions));
    }

    if (platforms != null && !platforms.isEmpty()) {
      spec = spec.and(UserSpecification.platformIn(platforms));
    }

    if (voice != null && !voice.isEmpty()) {
      spec = spec.and(UserSpecification.microphoneIn(voice));
    }

    if (onlyOnline) {
      spec = spec.and(UserSpecification.isOnline());
    }

    Page<User> users = userRepository.findAll(spec, pageable);

    List<UserCommunityResponse> communityResponse = users.stream()
          .map(foundUser -> communityMapper.toUserCommunityResponse(foundUser, user, friendIds))
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

  @Override
  public Long sendFriendRequest(final Authentication connectedUser, final Long userId) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    if (userId.equals(user.getId())) {
      throw new IllegalArgumentException("Cannot send friend request to yourself");
    }

    boolean exists = friendRequestRepository.existsBySender_IdAndReceiver_Id(user.getId(), userId);

    if (exists) {
      throw new IllegalStateException("Friend request already send");
    }

    User receiver = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("Receiver not found with id: " + userId));

    FriendRequest friendRequest = new FriendRequest();
    friendRequest.setSender(user);
    friendRequest.setReceiver(receiver);
    friendRequest.setCreatedAt(LocalDateTime.now());

    friendRequestRepository.save(friendRequest);
    return friendRequest.getId();
  }

  @Override
  @Transactional
  public void cancelFriendRequest(final Authentication connectedUser, final Long userId) {
    User authUser = (User) connectedUser.getPrincipal();
    User sender = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    User receiver = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("Receiver not found with id: " + userId));

    boolean exists = friendRequestRepository.existsBySender_IdAndReceiver_Id(sender.getId(), userId);

    if (!exists) {
      throw new IllegalStateException("No friend request to cancel");
    }

    friendRequestRepository.deleteBySender_IdAndReceiver_Id(sender.getId(), receiver.getId());
  }

  @Override
  @Transactional
  public void rejectFriendRequest(final Authentication connectedUser, final Long userId) {
    User authUser = (User) connectedUser.getPrincipal();
    User receiver = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    User sender = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("Receiver not found with id: " + userId));

    friendRequestRepository.deleteBySender_IdAndReceiver_Id(sender.getId(), receiver.getId());
  }

  @Transactional
  public Long acceptFriendRequest(final Authentication connectedUser, final Long userId) {
    User authUser = (User) connectedUser.getPrincipal();

    User receiver = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    User sender = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("Receiver not found with id: " + userId));

    Friendship receiverToSender = Friendship.builder()
        .user(receiver)
        .friend(sender)
        .build();

    Friendship senderToReceiver = Friendship.builder()
        .user(sender)
        .friend(receiver)
        .build();

    friendshipRepository.save(receiverToSender);
    friendshipRepository.save(senderToReceiver);

    friendRequestRepository.deleteBySender_IdAndReceiver_Id(sender.getId(), receiver.getId());

    return receiver.getId();
  }

  @Override
  public PageResponse<FriendRequestResponse> getAllMyFriendRequests(final Authentication connectedUser,
      final int page,
      final int size) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

    Page<FriendRequest> requests = friendRequestRepository.findAllByReceiver_Id(user.getId(), pageable);

    List<FriendRequestResponse> friendRequests = requests.stream()
        .map(communityMapper::toFriendRequestResponse)
        .toList();

    return new PageResponse<>(
        friendRequests,
        requests.getNumber(),
        requests.getSize(),
        requests.getTotalElements(),
        requests.getTotalPages(),
        requests.isFirst(),
        requests.isLast()
    );
  }

  @Override
  public List<PlatformResponse> getAllGamingPlatforms() {
    return Arrays.stream(Platform.values())
        .map(platform -> new PlatformResponse(platform.name(),
            platform.getPlatformName(), platform.getPlatformIcon()))
        .toList();
  }

  @Override
  public List<RegionResponse> getAllRegions() {
    return Arrays.stream(Region.values())
        .sorted(Comparator.comparing(Region::getRegionName))
        .map(region -> new RegionResponse(region.name(),
            region.getRegionName()))
        .toList();
  }

  @Override
  public List<MicrophoneUsageResponse> getAllMicrophoneUsages() {
    return Arrays.stream(MicrophoneUsage.values())
        .map(microphoneUsage -> new MicrophoneUsageResponse(microphoneUsage.name(),
            microphoneUsage.getMicrophoneUsageName(), microphoneUsage.getMicrophoneUsageIcon()))
        .toList();
  }

}
