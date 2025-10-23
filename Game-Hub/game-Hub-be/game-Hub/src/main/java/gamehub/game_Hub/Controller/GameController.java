package gamehub.game_Hub.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Service.GameRequest;
import gamehub.game_Hub.Service.GameResponse;
import gamehub.game_Hub.Service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("store")
@RequiredArgsConstructor
public class GameController {

  private final GameService gameService;


  @PostMapping("/add-game")
  public void addGame(@Valid @RequestBody GameRequest gameRequest) {
    gameService.save(gameRequest);
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

}
