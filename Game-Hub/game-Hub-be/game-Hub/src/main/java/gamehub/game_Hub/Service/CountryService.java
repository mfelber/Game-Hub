package gamehub.game_Hub.Service;

import java.util.List;

import gamehub.game_Hub.Response.CountryResponse;

public interface CountryService {

  List<CountryResponse> findAllCountries();

}
