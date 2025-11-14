package gamehub.game_Hub.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Service.WishlistService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("wishlist")
@RequiredArgsConstructor
public class WishlistController {

  private final WishlistService wishlistService;

  // Retrieve the wishlist of a user
  @GetMapping("/wishlist")
  public ResponseEntity<PageResponse<GameResponse>> getWishlist(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      final Authentication connectedUser) {
    return ResponseEntity.ok(wishlistService.FindAllWishlistGames(page, size, connectedUser));
  }

}
