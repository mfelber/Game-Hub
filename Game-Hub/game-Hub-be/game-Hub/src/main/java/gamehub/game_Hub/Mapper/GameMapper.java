package gamehub.game_Hub.Mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.File.FileUtils;
import gamehub.game_Hub.Module.AgeRating;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.Genre;
import gamehub.game_Hub.Module.Language;
import gamehub.game_Hub.Module.Platform;
import gamehub.game_Hub.Module.Subtitles;
import gamehub.game_Hub.Module.SystemRequirements;
import gamehub.game_Hub.Repository.AgeRatingRepository;
import gamehub.game_Hub.Repository.LanguageRepository;
import gamehub.game_Hub.Repository.PlatformRepository;
import gamehub.game_Hub.Repository.SubtitleRepository;
import gamehub.game_Hub.Repository.genre.GenreRepository;
import gamehub.game_Hub.Request.GameRequest;
import gamehub.game_Hub.Response.AgeRatingResponse;
import gamehub.game_Hub.Response.GamePreviewResponse;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Response.GenreResponse;
import gamehub.game_Hub.Response.LanguageResponse;
import gamehub.game_Hub.Response.PlatformResponse;
import gamehub.game_Hub.Response.SubtitleResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameMapper {

  private final AgeRatingRepository ageRatingRepository;

  private final GenreRepository genreRepository;

  private final PlatformRepository platformRepository;

  private final LanguageRepository languageRepository;

  private final SubtitleRepository subtitleRepository;

  public Game toGame(final GameRequest gameRequest) {

    AgeRating ageRating = ageRatingRepository.findById(gameRequest.ageRatingId())
        .orElseThrow(
            () -> new EntityNotFoundException("Age rating with " + gameRequest.ageRatingId() + " was not found"));

    Set<Genre> genres = new HashSet<>(genreRepository.findAllById(gameRequest.genresIds()));
    Set<Platform> platforms = new HashSet<>(platformRepository.findAllById(gameRequest.platformIds()));
    Set<Language> languages = new HashSet<>(languageRepository.findAllById(gameRequest.languageIds()));
    Set<Subtitles> subtitles = new HashSet<>(subtitleRepository.findAllById(gameRequest.subtitleIds()));

    SystemRequirements systemRequirements = SystemRequirements.builder()
        .cpu(gameRequest.cpu())
        .gpu(gameRequest.gpu())
        .ram(gameRequest.ram())
        .storage(gameRequest.storage())
        .build();

    boolean free = gameRequest.price() == 0;

    return Game.builder()
        .id(gameRequest.gameId())
        .title(gameRequest.title())
        .genres(genres)
        .description(gameRequest.description())
        .publisher(gameRequest.publisher())
        .developer(gameRequest.developer())
        .releaseYear(gameRequest.releaseYear())
        .price(gameRequest.price())
        .free(free)
        .ageRating(ageRating)
        .systemRequirements(systemRequirements)
        .platforms(platforms)
        .languages(languages)
        .subtitles(subtitles)
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
        .discountPrice(game.getDiscountPrice())
        .discountPercent(game.getDiscountPercent())
        .hasDiscount(game.isHasDiscount())
        .ageRating(new AgeRatingResponse(game.getId(), game.getAgeRating().getAgeRating(), game.getAgeRating().getAgeRatingColor()))
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

  public GamePreviewResponse toGamePreviewResponse(Game game) {

    return GamePreviewResponse.builder()
        .gameId(game.getId())
        .title(game.getTitle())
        .gameCoverImage(FileUtils.readCoverFromLocation(game.getGameCoverImage()))
        .price(game.getPrice())
        .discountPrice(game.getDiscountPrice())
        .discountPercent(game.getDiscountPercent())
        .hasDiscount(game.isHasDiscount())
        .build();
  }


}