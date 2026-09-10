package gamehub.game_Hub.Mapper;

import java.util.Objects;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserLibrary;
import gamehub.game_Hub.Response.RecentGamesResponse;
import gamehub.game_Hub.Response.UserLibraryResponse;

@Service
public class LibraryMapper {

  private final GameMapper gameMapper;

  public LibraryMapper(final GameMapper gameMapper) {
    this.gameMapper = gameMapper;
  }

  public UserLibraryResponse toUserLibraryResponse(User user, UserLibrary library) {
    Game game = library.getGame();

    boolean currentlyPlaying = user.getCurrentlyPlayingGame() != null && user.getCurrentlyPlayingGame()
        .getId()
        .equals(library.getGame().getId());

    return UserLibraryResponse.builder()
        .gameId(game.getId())
        .title(game.getTitle())
        .gameCoverImage(FileUtils.readCoverFromLocation(game.getGameCoverImage()))
        .installed(library.isInstalled())
        .favorite(library.isFavorite())
        .playtimeMinutes(library.getPlaytimeMinutes())
        .lastPlayed(library.getLastPlayed())
        .currentlyPlaying(currentlyPlaying)
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
