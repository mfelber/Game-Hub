package gamehub.game_Hub.Controller;

import java.nio.file.AccessDeniedException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Service.LibraryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("library")
@RequiredArgsConstructor
public class LibraryController {

  private final LibraryService libraryService;

  // Retrieve library of authenticated user
  @GetMapping("/library")
  public ResponseEntity<PageResponse<GameResponse>> getLibrary(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      final Authentication connectedUser) throws AccessDeniedException {
    return ResponseEntity.ok(libraryService.FindAllOwnedGames(page, size, connectedUser));
  }

  // Get all favorite games
  @GetMapping("/favorites")
  public ResponseEntity<PageResponse<GameResponse>> getFavorites(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      final Authentication connectedUser) {
    return ResponseEntity.ok(libraryService.findAllFavoriteGames(page, size, connectedUser));
  }

  // Check if game is favorite
  @GetMapping("/check/game/{gameId}/favorite")
  public ResponseEntity<Boolean> checkGameFavorite(@PathVariable final Long gameId, final Authentication connectedUser) {
    return ResponseEntity.ok(libraryService.checkGameFavorite(gameId, connectedUser));
  }

  // Allows a user to add game as favorite
  @PostMapping("/add/favorite/{gameId}")
  public ResponseEntity<Long> addGameToFavorites(@PathVariable final Long gameId,
      final Authentication connectedUser) {
    return ResponseEntity.ok(libraryService.addGameToFavorites(gameId, connectedUser));
  }

  // Allows a user to remove game from favorites
  @PostMapping("/add/remove/{gameId}")
  public ResponseEntity<Long> removeGameFromFavorites(@PathVariable final Long gameId,
      final Authentication connectedUser) {
    return ResponseEntity.ok(libraryService.removeGameFromFavorites(gameId, connectedUser));
  }

  // Allows a user to recommend a game
  @PostMapping("/recommend/game/{gameId}")
  public ResponseEntity<Long> recommendGame(@PathVariable final Long gameId, final Authentication connectedUser) {
    return ResponseEntity.ok(libraryService.recommendGame(gameId, connectedUser));
  }

  // Allows a user to remove recommendation for game
  @PostMapping("/remove/recommend/game/{gameId}")
  public ResponseEntity<Long> removeRecommendGame(@PathVariable final Long gameId, final Authentication connectedUser) {
    return ResponseEntity.ok(libraryService.removeRecommendGame(gameId, connectedUser));
  }

  // Check if game is recommended
  @GetMapping("/check/game/{gameId}/recommended")
  public ResponseEntity<Boolean> checkGameRecommended(@PathVariable final Long gameId, final Authentication connectedUser) {
    return ResponseEntity.ok(libraryService.checkGameRecommended(gameId, connectedUser));
  }

}
