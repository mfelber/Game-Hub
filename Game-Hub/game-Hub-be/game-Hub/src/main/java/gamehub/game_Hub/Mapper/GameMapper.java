package gamehub.game_Hub.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Service.GameRequest;
import gamehub.game_Hub.Service.GameResponse;
import gamehub.game_Hub.Service.GenreResponse;

@Service
public class GameMapper {

  public Game toGame(final GameRequest gameRequest) {
    return Game.builder()
        .id(gameRequest.gameId())
        .title(gameRequest.title())
        .genres(gameRequest.genres())
        .description(gameRequest.description())
        .publisher(gameRequest.publisher())
        .developer(gameRequest.developer())
        .releaseYear(gameRequest.releaseYear())
        .price(gameRequest.price())
        .build();
  }

  public GameResponse toGameResponse(Game game) {

    return GameResponse.builder()
        .gameId(game.getId())
        .title(game.getTitle())
        .genres(game.getGenres().stream()
            .map(g -> new GenreResponse(g.getId(), g.getName()))
            .collect(Collectors.toSet()))
        .releaseYear(game.getReleaseYear())
        .description(game.getDescription())
        .developer(game.getDeveloper())
        .publisher(game.getPublisher())
        .price(game.getPrice())
        // TODO
        // .imageCover(game.getImageCover().getBytes())
        .build();
  }

}
