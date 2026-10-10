package gamehub.game_Hub.Service;

import java.util.List;

import gamehub.game_Hub.Response.OperationSystemResponse;

public interface PlatformService {

  List<OperationSystemResponse> findAllPlatforms();

}
