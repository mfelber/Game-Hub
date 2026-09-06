package gamehub.game_Hub.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Request.UserCartRequest;
import gamehub.game_Hub.Response.CartResponse;
import gamehub.game_Hub.Service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("cart")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @GetMapping("/user")
  public ResponseEntity<CartResponse> getCart(
      final Authentication connectedUser
  ) {
    return ResponseEntity.ok(cartService.getCart(connectedUser));

  }

  @PostMapping("/add-to-cart")
  public ResponseEntity<Long> addGameToCart(final Authentication connectedUser, @RequestBody @Valid UserCartRequest userCartRequest){
    return ResponseEntity.ok(cartService.addGameToCart(connectedUser, userCartRequest));
  }

  @DeleteMapping("/remove-from-cart/{gameId}")
  public Long removeFromCart(final Authentication connectedUser, @PathVariable Long gameId) {
    return cartService.removeFromCart(connectedUser, gameId);
  }

}
