package gamehub.game_Hub.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Request.GameRequest;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Service.GameService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("store")
@RequiredArgsConstructor
public class GameController {

  private final GameService gameService;

  @PostMapping("/add-game")
  public ResponseEntity<Long> addGame(@Valid @RequestBody GameRequest gameRequest) {
    return ResponseEntity.ok(gameService.save(gameRequest));
  }

  @PostMapping(value = "/cover/{gameId}", consumes = "multipart/form-data")
  public ResponseEntity<?> uploadGameCoverImage(
      @PathVariable final Long gameId,
      @Parameter()
      @RequestPart("file") MultipartFile file,
      final Authentication connectedUser
      ){
    gameService.uploadGameCoverImage(gameId,file);
    return ResponseEntity.accepted().build();
  }


  @GetMapping("/game/{gameId}")
  public ResponseEntity<GameResponse> getGameById(@PathVariable final Long gameId) {
    return ResponseEntity.ok(gameService.findById(gameId));
  }

  @GetMapping("/all-games")
  public ResponseEntity<PageResponse<GameResponse>> findAllGames(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
    return ResponseEntity.ok(gameService.findAllGames(page,size));
  }

  @PostMapping("/buy/{gameId}")
  public ResponseEntity<Long> buyGame(@PathVariable final Long gameId, final Authentication connectedUser) {
    return ResponseEntity.ok(gameService.buyGame(gameId, connectedUser));
  }

  @PostMapping("/add/wishlist/{gameId}")
  public ResponseEntity<Long> addGameToWishlist(@PathVariable final Long gameId, final Authentication connectedUser) {
    return ResponseEntity.ok(gameService.addGameToWishList(gameId, connectedUser));
  }

  @PostMapping("/delete/wishlist/{gameId}")
  public ResponseEntity<Long> removeGameFromWishlist(@PathVariable final Long gameId, final Authentication connectedUser) {
    return ResponseEntity.ok(gameService.removeGameToWishList(gameId, connectedUser));
  }

  @GetMapping("/check/game/{gameId}/owned/")
  public ResponseEntity<Boolean> checkGameOwned(@PathVariable final Long gameId, final Authentication connectedUser) {
    return ResponseEntity.ok(gameService.checkGameOwned(gameId, connectedUser));
  }

  @GetMapping("/check/game/wishlist/{gameId}")
  public ResponseEntity<Boolean> checkGameInWishlist(@PathVariable final Long gameId, final Authentication connectedUser) {
    return ResponseEntity.ok(gameService.checkGameInWishlist(gameId, connectedUser));
  }


}
