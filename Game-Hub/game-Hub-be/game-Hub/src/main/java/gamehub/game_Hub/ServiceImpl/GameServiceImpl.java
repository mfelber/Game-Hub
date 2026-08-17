package gamehub.game_Hub.ServiceImpl;

import static gamehub.game_Hub.enums.AccountType.CHILD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.GameMapper;
import gamehub.game_Hub.Module.AgeRating;
import gamehub.game_Hub.Module.Badge;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.Genre;
import gamehub.game_Hub.Module.Language;
import gamehub.game_Hub.Module.Platform;
import gamehub.game_Hub.Module.Subtitles;
import gamehub.game_Hub.Module.SystemRequirements;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserGameId;
import gamehub.game_Hub.Module.User.UserLibrary;
import gamehub.game_Hub.Module.User.UserWishlistId;
import gamehub.game_Hub.Module.User.Wishlist;
import gamehub.game_Hub.Repository.AgeRatingRepository;
import gamehub.game_Hub.Repository.BadgeRepository;
import gamehub.game_Hub.Repository.LanguageRepository;
import gamehub.game_Hub.Repository.PlatformRepository;
import gamehub.game_Hub.Repository.SubtitleRepository;
import gamehub.game_Hub.Repository.UserLibraryRepository;
import gamehub.game_Hub.Repository.UserStoreFlagRepository;
import gamehub.game_Hub.Repository.WishlistRepository;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.genre.GenreRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.File.FileStorageService;
import gamehub.game_Hub.Request.GameRequest;
import gamehub.game_Hub.Request.GameUpdateRequest;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Service.GameService;
import gamehub.game_Hub.enums.GameUnitSize;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

  private final GameRepository gameRepository;

  private final UserRepository userRepository;

  private final GameMapper gameMapper;

  private final FileStorageService fileStorageService;

  private final BadgeRepository badgeRepository;

  private final UserProgressService userProgressService;

  private final UserStoreFlagRepository userStoreFlagRepository;

  private final UserLibraryRepository libraryRepository;

  private final WishlistRepository wishlistRepository;

  private final PlatformRepository platformRepository;

  private final GenreRepository genreRepository;

  private final LanguageRepository languageRepository;

  private final SubtitleRepository subtitleRepository;

  private final AgeRatingRepository ageRatingRepository;

  @Override
  public Long save(final GameRequest gameRequest) {
    Game game = gameMapper.toGame(gameRequest);
    return gameRepository.save(game).getId();
  }

  @Override
  public void uploadGameCoverImage(final Long gameId, final MultipartFile file) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + gameId));
    var gameCoverImage = fileStorageService.saveGameCoverImage(file, game.getId());
    System.out.printf("new image: " + gameCoverImage);
    game.setGameCoverImage(gameCoverImage);
    System.out.printf("before new image: " + game.getGameCoverImage());
    gameRepository.save(game);
  }

  @Override
  public GameResponse findById(final Long gameId) {
    return gameRepository.findById(gameId)
        .map(gameMapper::toGameResponse)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
  }

  @Override
  public PageResponse<GameResponse> findAllGames(Authentication connectedUser, final int page, final int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    List<String> excludeRatings = new ArrayList<>();
    if (user.getAccountType() == CHILD) {
      excludeRatings.add("PEGI 16");
      excludeRatings.add("PEGI 18");
    } else {
      if (userStoreFlagRepository.existsByUser_IdAndUserFlagType_FlagCodeAndValueTrue(user.getId(), "PEGI_16")) {
        excludeRatings.add("PEGI 16");
      }

      if (userStoreFlagRepository.existsByUser_IdAndUserFlagType_FlagCodeAndValueTrue(user.getId(), "PEGI_18")) {
        excludeRatings.add("PEGI 18");
      }
    }

    Page<Game> games;
    if (excludeRatings.isEmpty()) {
      games = gameRepository.findAll(pageable);
    } else {
      games = gameRepository.findAllByAgeRating_AgeRatingNotIn(excludeRatings, pageable);
    }

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

  @Transactional
  public Long buyGame(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    boolean alreadyOwned = libraryRepository.existsUserLibrariesByUserAndGame(user, game);

    if (!alreadyOwned) {
      UserLibrary userLibrary = UserLibrary.builder()
          .id(new UserGameId(user.getId(), game.getId()))
          .user(user)
          .game(game)
          .installed(false)
          .favorite(false)
          .playtimeMinutes(0)
          .createdAt(LocalDateTime.now())
          .build();
      libraryRepository.save(userLibrary);
    }

    boolean gameInWishlist = wishlistRepository.existsByUserAndGame(user, game);
    if (gameInWishlist) {
      wishlistRepository.deleteByUserAndGame(user, game);
    }

    addGameCollectorBadge(user);

    // TODO: Subtract the game price from the user's balance upon purchase

    return game.getId();
  }

  public void addGameCollectorBadge(User user) {

    int gameLibrarySize = user.getLibrary().size();
    Badge badgeToAdd = null;

    if (gameLibrarySize >= 101) {
      badgeToAdd = badgeRepository.findByName("GAME_COLLECTOR_LEVEL_5");
    } else if (gameLibrarySize >= 51) {
      badgeToAdd = badgeRepository.findByName("GAME_COLLECTOR_LEVEL_4");
    } else if (gameLibrarySize >= 31) {
      badgeToAdd = badgeRepository.findByName("GAME_COLLECTOR_LEVEL_3");
    } else if (gameLibrarySize >= 16) {
      badgeToAdd = badgeRepository.findByName("GAME_COLLECTOR_LEVEL_2");
    } else if (gameLibrarySize >= 4) {
      badgeToAdd = badgeRepository.findByName("GAME_COLLECTOR_LEVEL_1");
    }

    if (badgeToAdd != null && !user.getBadges().contains(badgeToAdd)) {
      user.getBadges().removeIf(badge -> badge.getName().startsWith("GAME_COLLECTOR"));

      userProgressService.awardBadgeAndXp(user, badgeToAdd);
    }

  }

  @Transactional
  public Long addGameToWishList(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    boolean gameInWishlist = wishlistRepository.existsByUserAndGame(user, game);
    if (gameInWishlist) {
      wishlistRepository.deleteByUserAndGame(user, game);
    }

    if (!gameInWishlist) {
      Wishlist wishlist = Wishlist.builder()
          .id(new UserWishlistId(user.getId(), game.getId()))
          .user(user)
          .game(game)
          .addedAt(LocalDateTime.now())
          .build();
      wishlistRepository.save(wishlist);
    }

    return game.getId();
  }

  @Override
  @Transactional
  public Long removeGameFromWishList(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    boolean gameInWishlist = wishlistRepository.existsByUserAndGame(user, game);

    if (gameInWishlist) {
      wishlistRepository.deleteByUserAndGame(user, game);
    }

    return game.getId();
  }

  @Transactional
  public Boolean checkGameOwned(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    return libraryRepository.existsUserLibrariesByUserAndGame(user, game);
  }

  @Override
  public Boolean checkGameInWishlist(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    return wishlistRepository.existsByUserAndGame(user, game);
  }

  @Override
  public Long update(final Long gameId, final GameUpdateRequest gameUpdateRequest) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("Game with id: " + gameId + " was not found"));

    Set<Genre> genres = gameUpdateRequest.genresIds()
        .stream()
        .map(genreRepository::getReferenceById)
        .collect(Collectors.toSet());

    Set<Platform> platforms = gameUpdateRequest.platformIds()
        .stream()
        .map(platformRepository::getReferenceById)
        .collect(Collectors.toSet());

    Set<Language> languages = gameUpdateRequest.languageIds()
        .stream()
        .map(languageRepository::getReferenceById)
        .collect(Collectors.toSet());

    Set<Subtitles> subtitles = gameUpdateRequest.subtitleIds()
        .stream()
        .map(subtitleRepository::getReferenceById)
        .collect(Collectors.toSet());

    AgeRating ageRating = ageRatingRepository.findById(gameUpdateRequest.ageRatingId())
        .orElseThrow(() -> new EntityNotFoundException("Age rating not found"));

    boolean freeGame = gameUpdateRequest.price() == 0 ||  (gameUpdateRequest.discountPercent() != null && gameUpdateRequest.discountPercent() == 100);
    boolean gameHasDiscount = gameUpdateRequest.discountPercent() != null;

    Double discountedPrice = null;

    game.setPrice(gameUpdateRequest.price());

    if (gameUpdateRequest.discountPercent() != null) {
       discountedPrice = BigDecimal.valueOf(
              game.getPrice() * (1 - (double) gameUpdateRequest.discountPercent() / 100))
          .setScale(2, RoundingMode.DOWN)
          .doubleValue();
    }

    game.setTitle(gameUpdateRequest.title());
    game.setDescription(gameUpdateRequest.description());
    game.setPublisher(gameUpdateRequest.publisher());
    game.setDeveloper(gameUpdateRequest.developer());
    game.setReleaseYear(gameUpdateRequest.releaseYear());
    game.setDiscountPercent(gameUpdateRequest.discountPercent());
    game.setDiscountPrice(discountedPrice);
    game.setHasDiscount(gameHasDiscount);
    game.setFree(freeGame);
    game.setGenres(genres);
    game.setPlatforms(platforms);
    game.setLanguages(languages);
    game.setSubtitles(subtitles);
    game.setAgeRating(ageRating);

    SystemRequirements systemRequirements = game.getSystemRequirements();
    if (systemRequirements == null) {
      systemRequirements = new SystemRequirements();
      game.setSystemRequirements(systemRequirements);
    }

    systemRequirements.setCpu(gameUpdateRequest.cpu());
    systemRequirements.setGpu(gameUpdateRequest.gpu());
    systemRequirements.setRam(gameUpdateRequest.ram());
    systemRequirements.setStorage(gameUpdateRequest.storage());
    systemRequirements.setGameUnitSize(GameUnitSize.valueOf(gameUpdateRequest.gameUnitSize()));

    return gameRepository.save(game).getId();
  }

}