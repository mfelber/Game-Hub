package gamehub.game_Hub.Service;

import java.util.List;

import gamehub.game_Hub.Response.GenreResponse;

public interface GenreService {

  List<GenreResponse> findAllGenres();

}
