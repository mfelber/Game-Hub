package gamehub.game_Hub.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Request.UserCartRequest;
import gamehub.game_Hub.Response.CartResponse;
import jakarta.validation.Valid;

public interface CartService {

  Long addGameToCart(Authentication connectedUser, @Valid UserCartRequest userCartRequest);

  CartResponse getCart(Authentication connectedUser);

  Long removeFromCart(Authentication connectedUser, Long gameId);

}
