package gamehub.game_Hub.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Module.embededIds.SavedLfgPostId;
import gamehub.game_Hub.Request.LfgCreationRequest;
import gamehub.game_Hub.Response.LfgResponse;
import gamehub.game_Hub.Response.lfg.LfgOptionsResponse;
import gamehub.game_Hub.Service.LfgService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("lfg")
@RequiredArgsConstructor
public class LfgController {

  private final LfgService lfgService;

  @PostMapping("/create")
  public ResponseEntity<Long> createLfg(Authentication connectedUser, @Valid @RequestBody LfgCreationRequest request) {
    return ResponseEntity.ok(lfgService.createLfg(connectedUser, request));
  }

  @GetMapping("/lfg-posts")
  public ResponseEntity<PageResponse<LfgResponse>> getAllActiveLfgPosts(Authentication connectedUser,
      @RequestParam(defaultValue = "") String query,
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
    return ResponseEntity.ok(lfgService.getAllActiveLfgPosts(connectedUser, query , page, size));
  }

  @GetMapping("/saved-lfg-posts")
  public ResponseEntity<PageResponse<LfgResponse>> getSavedLfgPosts(Authentication connectedUser,
      @RequestParam(defaultValue = "") String query,
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
    return ResponseEntity.ok(lfgService.getSavedLfgPosts(connectedUser, query , page, size));
  }

  @GetMapping("/my-lfg-posts-limit")
  public ResponseEntity<Integer> getMyLfgPostsLimit(Authentication connectedUser) {
    return ResponseEntity.ok(lfgService.getMyLfgPostsLimit(connectedUser));
  }

  // rename this to my posts and send my countActive posts
  @GetMapping("/my-active-posts-cout")
  public ResponseEntity<Integer> getCountOfActivePosts(Authentication connectedUser) {
    return ResponseEntity.ok(lfgService.getCountOfActivePosts(connectedUser));
  }

  @GetMapping("/my-saved-post-count")
  public ResponseEntity<Integer> getSavedLfgPostsCount(Authentication connectedUser) {
    return ResponseEntity.ok(lfgService.getSavedLfgPostsCount(connectedUser));
  }

  @PostMapping("/save-lfg-post/{lfgPostId}")
  public ResponseEntity<SavedLfgPostId> saveLfgPost(@PathVariable final Long lfgPostId, Authentication connectedUser) {
    return ResponseEntity.ok(lfgService.saveLfgPost(lfgPostId, connectedUser));
  }

  @DeleteMapping("/remove-saved-lfg-post/{lfgPostId}")
  public ResponseEntity<Void> removeSavedLfgPost(@PathVariable final Long lfgPostId, Authentication connectedUser) {
    lfgService.removeSavedLfgPost(lfgPostId, connectedUser);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/lfg-options")
  public ResponseEntity<LfgOptionsResponse> getLfgOptions(Authentication connectedUser) {
    return ResponseEntity.ok(lfgService.getLfgOptions(connectedUser));
  }


}
