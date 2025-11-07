package gamehub.game_Hub.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Response.GenreResponse;
import gamehub.game_Hub.Response.LocationResponse;
import gamehub.game_Hub.Service.LocationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("location")
@RequiredArgsConstructor
public class LocationController {

  private final LocationService locationService;

  @GetMapping("/locations")
  public ResponseEntity<List<LocationResponse>> getLocations() {
    return ResponseEntity.ok(locationService.findAllLocations());
  }

}
