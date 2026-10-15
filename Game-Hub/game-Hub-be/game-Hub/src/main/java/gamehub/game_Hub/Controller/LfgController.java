package gamehub.game_Hub.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Request.LfgCreationRequest;
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

  // @GetMapping("/lfg-posts")
  // public ResponseEntity<>

  @GetMapping("/lfg-options")
  public ResponseEntity<LfgOptionsResponse> getLfgOptions(Authentication connectedUser) {
    return ResponseEntity.ok(lfgService.getLfgOptions(connectedUser));
  }

}
