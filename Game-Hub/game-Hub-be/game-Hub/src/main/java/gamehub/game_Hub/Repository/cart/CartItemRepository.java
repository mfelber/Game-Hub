package gamehub.game_Hub.Repository.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.CartItem;
import gamehub.game_Hub.Module.User.UserCart;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  Optional<CartItem> findByCartAndGame(UserCart cart, Game game);

  Boolean existsByCart_User_IdAndGame_Id(Long cartUserId, Long gameId);


  Long countByCart_User_Id(Long cartUserId);

  Optional <CartItem> findByGameId(Long gameId);

}
