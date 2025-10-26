package gamehub.game_Hub.Mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.SystemRequirements;
import gamehub.game_Hub.Service.GameRequest;
import gamehub.game_Hub.Service.GameResponse;
import gamehub.game_Hub.Service.GenreResponse;
import gamehub.game_Hub.Service.LanguageResponse;
import gamehub.game_Hub.Service.PlatformResponse;
import gamehub.game_Hub.Service.SubtitleResponse;

@Service
public class GameMapper {

  public Game toGame(final GameRequest gameRequest) {

    SystemRequirements systemRequirements = SystemRequirements.builder()
        .cpu(gameRequest.cpu())
        .gpu(gameRequest.gpu())
        .ram(gameRequest.ram())
        .os(gameRequest.os())
        .storage(gameRequest.storage())
        .build();

    return Game.builder()
        .id(gameRequest.gameId())
        .title(gameRequest.title())
        .genres(gameRequest.genres())
        .description(gameRequest.description())
        .publisher(gameRequest.publisher())
        .developer(gameRequest.developer())
        .releaseYear(gameRequest.releaseYear())
        .price(gameRequest.price())
        .systemRequirements(systemRequirements)
        .platforms(gameRequest.platforms())
        .languages(gameRequest.languages())
        .subtitles(gameRequest.subtitles())
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
        .gameCoverImage(FileUtils.readCoverFromLocation(game.getGameCoverImage()))
        .systemRequirements(game.getSystemRequirements())
        .platforms(game.getPlatforms().stream()
            .map(g -> new PlatformResponse(g.getId(), g.getName()))
            .collect(Collectors.toSet()))
        .languages(game.getLanguages().stream()
            .map(g -> new LanguageResponse(g.getId(), g.getName()))
            .collect(Collectors.toSet()))
        .subtitles(game.getSubtitles().stream()
            .map(g -> new SubtitleResponse(g.getId(), g.getName()))
            .collect(Collectors.toSet()))
        .build();
  }

}
