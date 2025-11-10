package gamehub.game_Hub.Service;

import java.util.List;

import gamehub.game_Hub.Response.LocationResponse;

public interface LocationService {

  List<LocationResponse> findAllLocations();

}
