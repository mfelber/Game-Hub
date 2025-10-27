package gamehub.game_Hub.Service;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;

public interface WishlistService {

  PageResponse<GameResponse> FindAllWishlistGames(int page, int size, Authentication connectedUser);

}
