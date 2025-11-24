package gamehub.game_Hub.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Response.CommunityFlagsResponse;
import gamehub.game_Hub.Response.StoreFlagsResponse;
import gamehub.game_Hub.Service.FlagsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("flags")
@RequiredArgsConstructor
public class FlagsController {

  private final FlagsService flagsService;

  @GetMapping("/get/store/flags")
  public List<StoreFlagsResponse> getAllStoreFlags() {
    return flagsService.getStoreFlags();
  }

  @GetMapping("/get/community/flags")
  public List<CommunityFlagsResponse> getAllCommunityFlags() {
  return flagsService.getAllCommunityFlags();
  }
}
