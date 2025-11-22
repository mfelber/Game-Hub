package gamehub.game_Hub.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Response.FlagsResponse;
import gamehub.game_Hub.Response.StoreFlagsResponse;
import gamehub.game_Hub.Service.StoreFlagsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("flags")
@RequiredArgsConstructor
public class FlagsController {

  private final StoreFlagsService storeFlagsService;

  @GetMapping("/get/store/flags")
  public List<StoreFlagsResponse> getAllStoreFlags() {
    return storeFlagsService.getAllFlags();
  }

  @GetMapping("/get/community/flags")
  public List<FlagsResponse> getAllCommunityFlags() {
  return null;
  }
}
