package gamehub.game_Hub.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Response.CountryResponse;
import gamehub.game_Hub.Service.CountryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("country")
@RequiredArgsConstructor
public class CountryController {

  private final CountryService countryService;

  // Get all locations
  @GetMapping("/countries")
  public ResponseEntity<List<CountryResponse>> getAllCountries() {
    return ResponseEntity.ok(countryService.findAllCountries());
  }

}
