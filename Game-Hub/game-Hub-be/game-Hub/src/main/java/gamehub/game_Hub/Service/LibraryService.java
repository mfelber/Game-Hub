package gamehub.game_Hub.Service;

import java.nio.file.AccessDeniedException;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Response.GameResponse;

public interface LibraryService {

  PageResponse<GameResponse> FindAllOwnedGames(int page, int size, Authentication connectedUser)
      throws AccessDeniedException;

  Long addGameToFavorites(Long gameId, Authentication connectedUser);

  Long removeGameFromFavorites(Long gameId, Authentication connectedUser);

  Boolean checkGameFavorite(Long gameId, Authentication connectedUser);

}
