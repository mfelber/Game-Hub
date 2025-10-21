package gamehub.game_Hub.Service;

import gamehub.game_Hub.Common.PageResponse;

public interface GameService {

  void save(GameRequest gameRequest);

  GameResponse findById(Long gameId);

  PageResponse<GameResponse> findAllGames(int page, int size);

}
