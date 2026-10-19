package gamehub.game_Hub.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Common.PageResponse;
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
  public ResponseEntity<PageResponse<LfgResponse>> getAllActiveLfgs(Authentication connectedUser,
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
    return ResponseEntity.ok(lfgService.getAllActiveLfgs(connectedUser, page, size));
  }

  @GetMapping("/my-lfg-posts")
  public ResponseEntity<Integer> getMyLfgPosts(Authentication connectedUser) {
    return ResponseEntity.ok(lfgService.getMyLfgPosts(connectedUser));
  }

  @GetMapping("/lfg-options")
  public ResponseEntity<LfgOptionsResponse> getLfgOptions(Authentication connectedUser) {
    return ResponseEntity.ok(lfgService.getLfgOptions(connectedUser));
  }

  @GetMapping("/active-posts")
  public ResponseEntity<Integer> getCountOfActivePosts(Authentication connectedUser) {
    return ResponseEntity.ok(lfgService.getCountOfActivePosts(connectedUser));
  }

}
