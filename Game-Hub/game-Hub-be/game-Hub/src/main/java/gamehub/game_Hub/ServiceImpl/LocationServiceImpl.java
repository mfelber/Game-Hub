package gamehub.game_Hub.ServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.User.Location;
import gamehub.game_Hub.Response.LocationResponse;
import gamehub.game_Hub.Service.LocationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {



  @Override
  public List<LocationResponse> findAllLocations() {
    return Arrays.stream(Location.values()).map(location -> new LocationResponse(location.name(), location.getLocationIcon())).collect(
        Collectors.toList());
  }

}
