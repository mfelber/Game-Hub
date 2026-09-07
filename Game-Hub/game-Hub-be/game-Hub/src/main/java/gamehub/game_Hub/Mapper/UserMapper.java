package gamehub.game_Hub.Mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.BanHistory;
import gamehub.game_Hub.Module.Level;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserCart;
import gamehub.game_Hub.Module.User.UserLibrary;
import gamehub.game_Hub.Module.User.UserSuspensions;
import gamehub.game_Hub.Module.User.UserWarnings;
import gamehub.game_Hub.Repository.BanHistoryRepository;
import gamehub.game_Hub.Repository.FriendRequestRepository;
import gamehub.game_Hub.Repository.FriendshipRepository;
import gamehub.game_Hub.Repository.LevelRepository;
import gamehub.game_Hub.Repository.UserSuspensionRepository;
import gamehub.game_Hub.Repository.UserWarningsRepository;
import gamehub.game_Hub.Repository.cart.CartItemRepository;
import gamehub.game_Hub.Repository.cart.UserCartRepository;
import gamehub.game_Hub.Request.UserUpdateRequest;
import gamehub.game_Hub.Response.Admin.AdminUserResponse;
import gamehub.game_Hub.Response.BadgeResponse;
import gamehub.game_Hub.Response.CardColorResponse;
import gamehub.game_Hub.Response.GameResponseShort;
import gamehub.game_Hub.Response.GenreResponse;
import gamehub.game_Hub.Response.LevelProgressResponse;
import gamehub.game_Hub.Response.LevelResponse;
import gamehub.game_Hub.Response.LocationResponse;
import gamehub.game_Hub.Response.RecentUserResponse;
import gamehub.game_Hub.Response.StatusResponse;
import gamehub.game_Hub.Response.UserNotificationsResponse;
import gamehub.game_Hub.Response.UserPrivateResponse;
import gamehub.game_Hub.Response.UserPublicResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserMapper {

  private final LevelRepository levelRepository;

  private final UserWarningsRepository userWarningsRepository;

  private final UserSuspensionRepository userSuspensionRepository;

  private final BanHistoryRepository banHistoryRepository;

  private final UserCartRepository userCartRepository;

  private final CartItemRepository cartItemRepository;

  private final FriendRequestRepository friendRequestRepository;

  private final FriendshipRepository friendshipRepository;

  public User toUser(UserUpdateRequest userUpdateRequest) {
    return User.builder()
        .firstName(userUpdateRequest.getFirstName())
        .lastName(userUpdateRequest.getLastName())
        .email(userUpdateRequest.getEmail())
        .location(userUpdateRequest.getLocation())
        .bio(userUpdateRequest.getBio())
        .build();
  }

  public UserPublicResponse toUserPublicResponse(User profileUser, User authenticatedUser) {

    String joinedDate = profileUser.getCreatedAt().getMonth().name().charAt(0) + profileUser.getCreatedAt()
        .getMonth()
        .name()
        .substring(1)
        .toLowerCase() + " " + profileUser.getCreatedAt().getYear();

    Boolean isFriend = friendshipRepository.existsByUser_IdAndFriend_Id(profileUser.getId(), authenticatedUser.getId());

    Boolean friendReqSent = friendRequestRepository.existsBySender_IdAndReceiver_Id(authenticatedUser.getId(),
        profileUser.getId());

    Boolean friendReqReceived = friendRequestRepository.existsByReceiver_IdAndSender_Id(authenticatedUser.getId(),
        profileUser.getId());

    // TODO get reviews count when implementing reviews
    return UserPublicResponse.builder()
        .userId(profileUser.getId())
        .username(profileUser.getName())
        .bio(profileUser.getBio())
        .playTime(profileUser.getLibrary().stream().mapToInt(UserLibrary::getPlaytimeMinutes).sum())
        .reviews(0L)
        .joinedDate(joinedDate)
        .isFriend(isFriend)
        .friendRequestSent(friendReqSent)
        .friendRequestReceived(friendReqReceived)
        .location(
            new LocationResponse(
                profileUser.getLocation() != null ? profileUser.getLocation().name() : null,
                profileUser.getLocation() != null ? "/assets/flags/" + profileUser.getLocation().name().toLowerCase() + ".svg" : null
            )
        )
        .status(profileUser.getStatus())
        .accountStatus(profileUser.getAccountStatus())
        .friendsCount(profileUser.getFriends().size())
        .libraryCount(profileUser.getLibrary().size())
        .wishlistCount(profileUser.getWishlist().size())
        .level(new LevelResponse(profileUser.getLevel().getId(), profileUser.getLevel().getLevelNumber(), profileUser.getLevel().getLevelColor()))
        .badges(profileUser.getBadges().stream().map(badge -> new BadgeResponse(badge.getId(), badge.getName(),
            badge.getDescription(), badge.getIconPath())).collect(Collectors.toSet()))
        .playRecently(profileUser.getPlayRecently().stream().limit(5)
            .map(g -> new GameResponseShort(
                g.getId(), g.getTitle(), FileUtils.readCoverFromLocation(g.getGameCoverImage())))
            .collect(Collectors.toSet()))
        .favoriteGenres(profileUser.getFavoriteGenres().stream()
            .map(g -> new GenreResponse(g.getId(), g.getName()))
            .collect(Collectors.toSet()))
        .recommendedGames(profileUser.getRecommendationGames()
            .stream()
            .map(g -> new GameResponseShort(g.getId(), g.getTitle(),
                FileUtils.readCoverFromLocation(g.getGameCoverImage())))
            .collect(Collectors.toSet()))
        .userProfilePicture(FileUtils.readCoverFromLocation(profileUser.getUserProfilePicture()))
        .bannerImage(FileUtils.readCoverFromLocation(profileUser.getBanner()))
        .profileColor(profileUser.getProfileColor())
        .bannerType(profileUser.getBannerType())
        .predefinedBannerPath(profileUser.getBanner())
        .cardColor(new CardColorResponse(profileUser.getCardColor().getId(),
            profileUser.getCardColor().getColorName(),
            profileUser.getCardColor().getColorCode()))
        .build();
  }

  public UserPrivateResponse toUserPrivateResponse(User user) {

    String joinedDate = user.getCreatedAt().getMonth().name().charAt(0) + user.getCreatedAt()
        .getMonth()
        .name()
        .substring(1)
        .toLowerCase() + " " + user.getCreatedAt().getYear();


    // TODO get reviews count when implementing reviews
    return UserPrivateResponse.builder()
        .userId(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .username(user.getName())
        .playTime(user.getLibrary().stream().mapToInt(UserLibrary::getPlaytimeMinutes).sum())
        .reviews(0L)
        .bio(user.getBio())
        .joinedDate(joinedDate)
        .location(
            new LocationResponse(
                user.getLocation() != null ? user.getLocation().name() : null,
                user.getLocation() != null ? "/assets/flags/" + user.getLocation().name().toLowerCase() + ".svg" : null
            )
        )
        .status(user.getStatus())
        .friendsCount(user.getFriends().size())
        .libraryCount(user.getLibrary().size())
        .wishlistCount(user.getWishlist().size())
        .level(new LevelResponse(user.getLevel().getId(), user.getLevel().getLevelNumber(),user.getLevel().getLevelColor()))
        .badges(user.getBadges().stream().map(badge -> new BadgeResponse(badge.getId(), badge.getName(),
            badge.getDescription(), badge.getIconPath())).collect(Collectors.toSet()))
        .playRecently(user.getPlayRecently().stream().limit(5)
            .map(g -> new GameResponseShort(
                g.getId(), g.getTitle(), FileUtils.readCoverFromLocation(g.getGameCoverImage())))
            .collect(Collectors.toSet()))
        .favoriteGenres(user.getFavoriteGenres()
            .stream()
            .map(genre -> new GenreResponse(genre.getId(), genre.getName()))
            .collect(Collectors.toSet()))
        .recommendedGames(user.getRecommendationGames()
            .stream()
            .map(g -> new GameResponseShort(g.getId(), g.getTitle(),
                FileUtils.readCoverFromLocation(g.getGameCoverImage())))
            .collect(Collectors.toSet()))
        .userProfilePicture(FileUtils.readCoverFromLocation(user.getUserProfilePicture()))
        .bannerImage(FileUtils.readCoverFromLocation(user.getBanner()))
        .profileColor(user.getProfileColor())
        .bannerType(user.getBannerType())
        .predefinedBannerPath(user.getBanner())
        .cardColor(new CardColorResponse(user.getCardColor().getId(),
            user.getCardColor().getColorName(),
            user.getCardColor().getColorCode()))
        .build();
  }

  public StatusResponse toUserStatusResponse(final User user) {
    return StatusResponse.builder()
        .status(user.getStatus())
        .build();
  }

  public UserPrivateResponse toUserPrivateResponseShort(User user) {
    return UserPrivateResponse.builder()
        .userId(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .username(user.getName())
        .status(user.getStatus())
        .userProfilePicture(FileUtils.readCoverFromLocation(user.getUserProfilePicture()))
        .profileColor(user.getProfileColor())
        .build();
  }

  public UserPrivateResponse toUserBioResponse(final User user) {
    return UserPrivateResponse.builder().bio(user.getBio()).build();
  }

  public LevelProgressResponse toUserLevelProgress(final User user) {

    Level nextLevel = levelRepository.findById(user.getLevel().getId() + 1).orElseThrow(() -> new EntityNotFoundException("No user found with id: "));

    Long requiredXP = nextLevel.getRequiredXp();
    Long userXP = user.getXp();

    return LevelProgressResponse.builder()
        .level(new LevelResponse(user.getLevel().getId(), user.getLevel().getLevelNumber(),user.getLevel().getLevelColor()))
        .currentXp(user.getXp())
        .requiredXp(requiredXP)
        .nextLevel(nextLevel.getLevelNumber())
        .build();
  }

  public RecentUserResponse toRecentUserResponse(User user) {
    return RecentUserResponse.builder()
        .id(user.getId())
        .username(user.getName())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .build();
  }

  public AdminUserResponse toAdminUserResponse(User user) {

    BanHistory activeBan =
        banHistoryRepository.findFirstByUserOrderByBannedAtDesc(user)
            .orElse(null);

    UserSuspensions activeSuspension = userSuspensionRepository.findFirstByUserOrderByCreatedAtDesc(user);

    return AdminUserResponse.builder()
        .userId(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .userName(user.getName())
        .email(user.getEmail())
        .profilePicture(FileUtils.readCoverFromLocation(user.getUserProfilePicture()))
        .profileColor(user.getProfileColor())
        .bio(user.getBio())
        .role(user.getRole())
        .accountType(user.getAccountType())
        .accountLevel(user.getLevel().getLevelNumber())
        .location(new LocationResponse(
            user.getLocation() != null ? user.getLocation().name() : null,
            user.getLocation() != null ? "assets/flags/" + user.getLocation().name().toLowerCase() + ".svg" : null
        ))
        .accountStatus(user.getAccountStatus())
        .registered(user.getCreatedAt())
        .lastLogin(user.getLastLogin())
        .lastModifiedAt(user.getLastModifiedAt())
        .banReason(activeBan != null ? activeBan.getReason().getCommunityGuideline() : null)
        .bannedAt(activeBan != null ? activeBan.getBannedAt() : null)
        .suspendedReason(activeSuspension != null ? activeSuspension.getSuspensionReason().getCommunityGuideline() : null)
        .suspendedAt(activeSuspension != null ? activeSuspension.getCreatedAt() : null)
        .build();
  }

  public UserNotificationsResponse toUserNotificationResponse(final User user) {

    Long warnings = userWarningsRepository.countByUser(user);
    Long suspensions = userSuspensionRepository.countByUser(user);
    Long cartItems = cartItemRepository.countByCart_User_Id(user.getId());

    return UserNotificationsResponse.builder()
        .warningCount(warnings !=null ? warnings : null)
        .suspendedCount(suspensions !=null ? suspensions : null)
        .cartCount(cartItems != null ? cartItems : null)
        .build();
  }

}