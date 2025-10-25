package gamehub.game_Hub.Service;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;

public interface GameService {

  void save(GameRequest gameRequest);

  GameResponse findById(Long gameId);

  PageResponse<GameResponse> findAllGames(int page, int size);

  Long buyGame(Long gameId, Authentication connectedUser);

  Boolean checkGameOwned(Long gameId, Authentication connectedUser);

}
