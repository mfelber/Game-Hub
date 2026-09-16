package gamehub.game_Hub.ServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.enums.Location;
import gamehub.game_Hub.Response.CountryResponse;
import gamehub.game_Hub.Service.CountryService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

  @Override
  public List<CountryResponse> findAllCountries() {
    return Arrays.stream(Location.values())
        .map(location -> new CountryResponse(location.name(), location.getLocationIcon()))
        .collect(
            Collectors.toList());
  }

}
