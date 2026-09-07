package gamehub.game_Hub.Mapper;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.Wishlist;
import gamehub.game_Hub.Response.WishlistResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistMapper {

  private final GameMapper gameMapper;

  public WishlistResponse toWishlistResponse(User connectedUser, Wishlist wishlist) {
    Game game = wishlist.getGame();

    return WishlistResponse.builder()
        .addedAt(wishlist.getAddedAt())
        .game(gameMapper.toGamePreviewResponse(connectedUser, game))
        .build();
  }

}
