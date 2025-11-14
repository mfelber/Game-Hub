package gamehub.game_Hub.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Repository.genre.GenreRepository;
import gamehub.game_Hub.Response.GenreResponse;
import gamehub.game_Hub.Service.GenreService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

  private final GenreRepository genreRepository;

  @Override
  public List<GenreResponse> findAllGenres() {
    return genreRepository.findAll().stream().map(genre -> new GenreResponse(genre.getId(), genre.getName())).toList();
  }

}
