package gamehub.game_Hub.Service;

import java.util.List;

import gamehub.game_Hub.Response.LanguageResponse;

public interface LanguageService {

  List<LanguageResponse> findAllLanguages();

}
