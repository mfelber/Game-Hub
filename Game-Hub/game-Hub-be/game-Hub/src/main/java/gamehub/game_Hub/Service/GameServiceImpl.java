package gamehub.game_Hub.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.GameMapper;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

  private final GameRepository gameRepository;

  private final GameMapper gameMapper;

  @Override
  public void save(final GameRequest gameRequest) {
    Game game = gameMapper.toGame(gameRequest);
    gameRepository.save(game);


  }

  @Override
  public GameResponse findById(final Long gameId) {
    return gameRepository.findById(gameId)
        .map(gameMapper::toGameResponse)
        .orElseThrow(()-> new EntityNotFoundException("No game found with id: " + gameId));
  }

  @Override
  public PageResponse<GameResponse> findAllGames(final int page, final int size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<Game> games = gameRepository.findAll(pageable);

    // System.out.println(games.getContent());

    List<GameResponse> gameResponse = games.stream().map(gameMapper::toGameResponse).toList();

    return new PageResponse<>(
        gameResponse,
        games.getNumber(),
        games.getSize(),
        games.getTotalElements(),
        games.getTotalPages(),
        games.isFirst(),
        games.isLast()
    );

  }

}
