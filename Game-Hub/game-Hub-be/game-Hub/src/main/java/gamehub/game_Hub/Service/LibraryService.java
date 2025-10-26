package gamehub.game_Hub.Service;

import java.nio.file.AccessDeniedException;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;

public interface LibraryService {

  PageResponse<GameResponse> FindAllOwnedGames(int page, int size, Authentication connectedUser)
      throws AccessDeniedException;
}
