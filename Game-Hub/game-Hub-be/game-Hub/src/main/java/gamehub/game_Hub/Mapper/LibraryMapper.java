package gamehub.game_Hub.Mapper;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.Genre;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserLibrary;
import gamehub.game_Hub.Response.AgeRatingResponse;
import gamehub.game_Hub.Response.GenreResponse;
import gamehub.game_Hub.Response.RecentGamesResponse;
import gamehub.game_Hub.Response.UserLibraryResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibraryMapper {

  private final GameMapper gameMapper;

  public UserLibraryResponse toUserLibraryResponse(User user, UserLibrary library) {
    Game game = library.getGame();

    boolean currentlyPlaying = user.getCurrentlyPlayingGame() != null && user.getCurrentlyPlayingGame()
        .getId()
        .equals(library.getGame().getId());

    return UserLibraryResponse.builder()
        .gameId(game.getId())
        .title(game.getTitle())
        .description(game.getDescription())
        .developer(game.getDeveloper())
        .publisher(game.getPublisher())
        .releaseYear(game.getReleaseYear())
        .gameCoverImage(FileUtils.readCoverFromLocation(game.getGameCoverImage()))
        .installed(library.isInstalled())
        .favorite(library.isFavorite())
        .playtimeMinutes(library.getPlaytimeMinutes())
        .currentlyPlaying(currentlyPlaying)
        .genres(game.getGenres().stream().sorted(Comparator.comparing(Genre::getName))
            .map(g -> new GenreResponse(g.getId(), g.getName()))
            .collect(Collectors.toList()))
        .ageRating(new AgeRatingResponse(game.getAgeRating().getId(), game.getAgeRating().getAgeRating(), game.getAgeRating().getAgeRatingColor()))
        .build();
  }

  public UserLibraryResponse toCurrentlyPlayingGameResponse(Game game) {
    return UserLibraryResponse.builder()
        .gameId(game.getId())
        .title(game.getTitle())
        .build();
  }

  public UserLibraryResponse toFavoriteGameResponse(User user) {
    Game favoriteGame = user.getFavoriteGame();

    if (favoriteGame == null) {
      return null;
    }

    return user.getLibrary().stream().filter(library -> library.getGame().getId().equals(favoriteGame.getId()))
        .findFirst()
        .map(library -> UserLibraryResponse.builder()
            .gameId(favoriteGame.getId())
            .title(favoriteGame.getTitle())
            .gameCoverImage(FileUtils.readCoverFromLocation(favoriteGame.getGameCoverImage()))
            .playtimeMinutes(library.getPlaytimeMinutes())
            .build()).orElse(null);
  }

  public RecentGamesResponse toRecentGamesResponse(UserLibrary library) {
    return RecentGamesResponse.builder()
        .game(gameMapper.toGameResponseShort(library.getGame()))
        .lastPlayed(library.getPlayedAt())
        .playTime(library.getPlaytimeMinutes())
        .build();
  }

}
