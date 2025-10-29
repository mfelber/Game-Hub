package gamehub.game_Hub.Mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Request.UserUpdateRequest;
import gamehub.game_Hub.Response.BadgeResponse;
import gamehub.game_Hub.Response.GameResponseShort;
import gamehub.game_Hub.Response.GenreResponse;
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
    return UserPublicResponse.builder()
        .userId(user.getId())
        .username(user.getName())
        .bio(user.getBio())
        .location(user.getLocation())
        .libraryCount(user.getLibrary().size())
        .wishlistCount(user.getWishlist().size())
        .badges(user.getBadges().stream()
            .map(b -> new BadgeResponse(
                b.getId(), b.getName(), b.getDescription(), b.getBadgeImage())).collect(
            Collectors.toSet()))
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
        .userProfilePicture(FileUtils.readCoverFromLocation(user.getUserProfilePicture()))
        .build();
  }

  public UserPrivateResponse toUserPrivateResponse(User user) {
    return UserPrivateResponse.builder()
        .userId(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .username(user.getName())
        .bio(user.getBio())
        .location(user.getLocation())
        .libraryCount(user.getLibrary().size())
        .wishlistCount(user.getWishlist().size())
        .badges(user.getBadges().stream()
            .map(b -> new BadgeResponse(
                b.getId(), b.getName(), b.getDescription(), b.getBadgeImage())).collect(
                Collectors.toSet()))
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
        .userProfilePicture(FileUtils.readCoverFromLocation(user.getUserProfilePicture()))
        .build();

  }

}
