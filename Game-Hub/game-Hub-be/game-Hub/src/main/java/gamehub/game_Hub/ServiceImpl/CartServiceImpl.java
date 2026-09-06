package gamehub.game_Hub.ServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Mapper.CartMapper;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.CartItem;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserCart;
import gamehub.game_Hub.Repository.cart.CartItemRepository;
import gamehub.game_Hub.Repository.cart.UserCartRepository;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Request.UserCartRequest;
import gamehub.game_Hub.Response.CartResponse;
import gamehub.game_Hub.Service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

  private final UserRepository userRepository;

  private final UserCartRepository userCartRepository;

  private final GameRepository gameRepository;

  private final CartItemRepository cartItemRepository;

  private final CartMapper cartMapper;

  @Override
  public Long addGameToCart(final Authentication connectedUser, final UserCartRequest userCartRequest) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    UserCart cart = userCartRepository.findByUser(user).orElseGet(() -> {
      UserCart newCart = UserCart.builder()
          .user(user)
          .build();
      return userCartRepository.save(newCart);
    });

    Game game = gameRepository.findById(userCartRequest.gameId())
        .orElseThrow(() -> new EntityNotFoundException("Game with id: " + userCartRequest.gameId() + " was not found"));

    CartItem cartItem = CartItem.builder()
        .cart(cart)
        .game(game)
        .build();

    if (cartItemRepository.findByCartAndGame(cart, game).isPresent()) {
      throw new IllegalStateException("Game is already in cart");
    }

    cartItemRepository.save(cartItem);

    return cartItem.getId();
  }

  @Override
  public Long removeFromCart(final Authentication connectedUser, final Long gameId) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("Game with id: " + gameId + " was not found"));

    UserCart cart = userCartRepository.findByUser(user)
        .orElseThrow(() -> new EntityNotFoundException("Cart not found for user: " + user.getId()));

    CartItem cartItem = cartItemRepository.findByCartAndGame(cart, game)
        .orElseThrow(() -> new EntityNotFoundException("Game with id: " + gameId + " is not in cart"));

    cartItemRepository.delete(cartItem);

    return cartItem.getId();
  }

  @Override
  public CartResponse getCart(final Authentication connectedUser) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    UserCart cart = userCartRepository.findByUser(user).orElse(null);

    if (cart == null) {
      return cartMapper.toEmptyCartResponse();
    }

    return cartMapper.toCartResponse(cart);
  }

}
