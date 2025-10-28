package gamehub.game_Hub.Service;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Response.GameResponse;

public interface WishlistService {

  PageResponse<GameResponse> FindAllWishlistGames(int page, int size, Authentication connectedUser);

}
