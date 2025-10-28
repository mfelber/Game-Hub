package gamehub.game_Hub.Controller;

import java.nio.file.AccessDeniedException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Service.LibraryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("library")
@RequiredArgsConstructor
public class LibraryController {

  private final UserRepository userRepository;

  private final LibraryService libraryService;

  @GetMapping("/library")
  public ResponseEntity<PageResponse<GameResponse>> getLibrary(
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size,
      final Authentication connectedUser) throws AccessDeniedException {
    return ResponseEntity.ok(libraryService.FindAllOwnedGames(page, size, connectedUser));
  }

  @PutMapping("/add/favorite/{gameId}")
  public ResponseEntity<Long> addGameToFavorites(@PathVariable final Long gameId, final Authentication connectedUser) {
    return ResponseEntity.ok(libraryService.addGameToFavorites(gameId, connectedUser));
  }

}
