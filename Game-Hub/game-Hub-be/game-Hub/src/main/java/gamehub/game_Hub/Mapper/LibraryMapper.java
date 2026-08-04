package gamehub.game_Hub.Mapper;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.UserLibrary;
import gamehub.game_Hub.Response.UserLibraryResponse;

@Service
public class LibraryMapper {

  public UserLibraryResponse toUserLibraryResponse(UserLibrary library) {
    Game game = library.getGame();
    return UserLibraryResponse.builder()
        .gameId(game.getId())
        .title(game.getTitle())
        .gameCoverImage(FileUtils.readCoverFromLocation(game.getGameCoverImage()))
        .installed(library.isInstalled())
        .favorite(library.isFavorite())
        .playtimeMinutes(library.getPlaytimeMinutes())
        .lastPlayed(library.getLastPlayed())
        .build();
  }

}
