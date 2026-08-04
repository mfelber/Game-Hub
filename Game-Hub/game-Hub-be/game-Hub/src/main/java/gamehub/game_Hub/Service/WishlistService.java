package gamehub.game_Hub.Service;

import java.nio.file.AccessDeniedException;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Response.UserLibraryResponse;
import gamehub.game_Hub.Response.WishlistResponse;

public interface WishlistService {

  PageResponse<WishlistResponse> getUserWishlist(int page, int size, Authentication connectedUser)
      throws AccessDeniedException;

}
