package gamehub.game_Hub.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Response.UserCommunityResponse;
import gamehub.game_Hub.Service.CommunityService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("community")
@RequiredArgsConstructor
public class CommunityController {

  private final CommunityService communityService;

  @GetMapping("/get/all/users")
  public ResponseEntity<PageResponse<UserCommunityResponse>> findAllUsers(Authentication connectedUser,
      @RequestParam(defaultValue = "") String query,
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
    return ResponseEntity.ok(communityService.findAllUsers(connectedUser,query, page, size));
  }
}
