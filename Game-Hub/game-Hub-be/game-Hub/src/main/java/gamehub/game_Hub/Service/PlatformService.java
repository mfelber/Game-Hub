package gamehub.game_Hub.Service;

import java.util.List;

import gamehub.game_Hub.Response.PlatformResponse;

public interface PlatformService {

  List<PlatformResponse> findAllPlatforms();

}
