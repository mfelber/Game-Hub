package gamehub.game_Hub.Mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Request.UserUpdateRequest;
import gamehub.game_Hub.Response.BadgeResponse;
import gamehub.game_Hub.Response.GameResponseShort;
import gamehub.game_Hub.Response.GenreResponse;
import gamehub.game_Hub.Response.LocationResponse;
import gamehub.game_Hub.Response.StatusResponse;
import gamehub.game_Hub.Response.UserPrivateResponse;
import gamehub.game_Hub.Response.UserPublicResponse;

@Service
public class UserMapper {

  public User toUser(UserUpdateRequest userUpdateRequest) {
    return User.builder()
        .firstName(userUpdateRequest.getFirstName())
        .lastName(userUpdateRequest.getLastName())
        .email(userUpdateRequest.getEmail())
        .location(userUpdateRequest.getLocation())
        .bio(userUpdateRequest.getBio())
        .build();
  }

  public UserPublicResponse toUserPublicResponse(User user) {

    String joinedDate = user.getCreatedAt().getMonth().name().charAt(0) + user.getCreatedAt()
        .getMonth()
        .name()
        .substring(1)
        .toLowerCase() + " " + user.getCreatedAt().getYear();

    return UserPublicResponse.builder()
        .userId(user.getId())
        .username(user.getName())
        .bio(user.getBio())
        .joinedDate(joinedDate)
        .location(
            new LocationResponse(
                user.getLocation() != null ? user.getLocation().name() : null,
                user.getLocation() != null ? "/assets/flags/" + user.getLocation().name().toLowerCase() + ".svg" : null
            )
        )
        .status(user.getStatus())
        .libraryCount(user.getLibrary().size())
        .wishlistCount(user.getWishlist().size())
        .badges(user.getBadges().stream().map(badge -> new BadgeResponse(badge.getId(), badge.getName(),
            badge.getDescription(), badge.getIconPath())).collect(Collectors.toSet()))
        .playRecently(user.getPlayRecently().stream().limit(5)
            .map(g -> new GameResponseShort(
                g.getId(), g.getTitle(), FileUtils.readCoverFromLocation(g.getGameCoverImage())))
            .collect(Collectors.toSet()))
        .favoriteGames(user.getFavoriteGames().stream()
            .map(g -> new GameResponseShort(
                g.getId(), g.getTitle(), FileUtils.readCoverFromLocation(g.getGameCoverImage())))
            .collect(Collectors.toSet()))
        .favoriteGenres(user.getFavoriteGenres().stream()
            .map(g -> new GenreResponse(g.getId(), g.getName()))
            .collect(Collectors.toSet()))
        .recommendedGames(user.getRecommendationGames()
            .stream()
            .map(g -> new GameResponseShort(g.getId(), g.getTitle(),
                FileUtils.readCoverFromLocation(g.getGameCoverImage())))
            .collect(Collectors.toSet()))
        .userProfilePicture(FileUtils.readCoverFromLocation(user.getUserProfilePicture()))
        .bannerImage(FileUtils.readCoverFromLocation(user.getBanner()))
        .profileColor(user.getProfileColor())
        .build();
  }

  public UserPrivateResponse toUserPrivateResponse(User user) {

    String joinedDate = user.getCreatedAt().getMonth().name().charAt(0) + user.getCreatedAt()
        .getMonth()
        .name()
        .substring(1)
        .toLowerCase() + " " + user.getCreatedAt().getYear();

    return UserPrivateResponse.builder()
        .userId(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .username(user.getName())
        .bio(user.getBio())
        .joinedDate(joinedDate)
        .location(
            new LocationResponse(
                user.getLocation() != null ? user.getLocation().name() : null,
                user.getLocation() != null ? "/assets/flags/" + user.getLocation().name().toLowerCase() + ".svg" : null
            )
        )
        .status(user.getStatus())
        .libraryCount(user.getLibrary().size())
        .wishlistCount(user.getWishlist().size())
        .badges(user.getBadges().stream().map(badge -> new BadgeResponse(badge.getId(), badge.getName(),
            badge.getDescription(), badge.getIconPath())).collect(Collectors.toSet()))
        .playRecently(user.getPlayRecently().stream().limit(5)
            .map(g -> new GameResponseShort(
                g.getId(), g.getTitle(), FileUtils.readCoverFromLocation(g.getGameCoverImage())))
            .collect(Collectors.toSet()))
        .favoriteGames(user.getFavoriteGames().stream()
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
        .build();
  }

  public UserPrivateResponse toUserBioResponse(final User user) {
    return UserPrivateResponse.builder().bio(user.getBio()).build();
  }

}
