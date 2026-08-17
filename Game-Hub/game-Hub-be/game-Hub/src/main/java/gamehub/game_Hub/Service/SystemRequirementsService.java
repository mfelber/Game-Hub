package gamehub.game_Hub.Service;

import java.util.List;

import gamehub.game_Hub.Response.UnitSizeResponse;

public interface SystemRequirementsService {

  List<UnitSizeResponse> findAllUnitSizes();

}
