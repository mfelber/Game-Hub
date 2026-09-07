package gamehub.game_Hub.Mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.User.CartItem;
import gamehub.game_Hub.Module.User.UserCart;
import gamehub.game_Hub.Response.CartResponse;
import gamehub.game_Hub.Response.GameResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartMapper {

  private final GameMapper gameMapper;

  public CartResponse toCartResponse(UserCart cart) {
    List<GameResponse> games = cart.getItems()
        .stream()
        .map(CartItem::getGame)
        .map(gameMapper::toGameResponse)
        .toList();

    return CartResponse.builder()
        .cartId(cart.getId())
        .games(games)
        .build();
  }

  public CartResponse toEmptyCartResponse() {
    return CartResponse.builder()
        .games(List.of())
        .build();
  }

}
