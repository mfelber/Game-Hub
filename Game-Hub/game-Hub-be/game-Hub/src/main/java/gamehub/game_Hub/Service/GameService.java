package gamehub.game_Hub.Service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import gamehub.game_Hub.Common.PageResponse;

public interface GameService {

  Long save(GameRequest gameRequest);

  GameResponse findById(Long gameId);

  PageResponse<GameResponse> findAllGames(int page, int size);

  Long buyGame(Long gameId, Authentication connectedUser);

  Boolean checkGameOwned(Long gameId, Authentication connectedUser);

  void uploadGameCoverImage(Long gameId, MultipartFile file);

  Long addGameToWishList(Long gameId, Authentication connectedUser);

  Long removeGameToWishList(Long gameId, Authentication connectedUser);

  Boolean checkGameInWishlist(Long gameId, Authentication connectedUser);

}
