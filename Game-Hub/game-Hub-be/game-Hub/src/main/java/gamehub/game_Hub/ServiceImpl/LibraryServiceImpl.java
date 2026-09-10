package gamehub.game_Hub.ServiceImpl;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.GameMapper;
import gamehub.game_Hub.Mapper.LibraryMapper;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserLibrary;
import gamehub.game_Hub.Repository.UserLibraryRepository;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.RecentGamesResponse;
import gamehub.game_Hub.Response.UserLibraryResponse;
import gamehub.game_Hub.Service.LibraryService;
import gamehub.game_Hub.enums.Status;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

  private final UserRepository userRepository;

  private final GameMapper gameMapper;

  private final GameRepository gameRepository;

  private final UserLibraryRepository libraryRepository;

  private final LibraryMapper libraryMapper;

  @Override
  @PreAuthorize("isAuthenticated()")
  public PageResponse<UserLibraryResponse> getUserLibrary(final int page, final int size,
      final Authentication connectedUser)
      throws AccessDeniedException {
    if (connectedUser == null || !connectedUser.isAuthenticated()) {
      throw new AccessDeniedException("User is not authenticated");
    }

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Pageable pageable = PageRequest.of(page, size, Sort.by("game").ascending());
    Page<UserLibrary> library = libraryRepository.findUserLibrariesByUser(user, pageable);

    List<UserLibraryResponse> libraryResponse = library.getContent()
        .stream()
        .sorted(Comparator.comparing(lib -> lib.getGame().getTitle()))
        .map(userLibrary -> libraryMapper.toUserLibraryResponse(user, userLibrary))
        .toList();

    return new PageResponse<>(
        libraryResponse,
        library.getNumber(),
        library.getSize(),
        library.getTotalElements(),
        library.getTotalPages(),
        library.isFirst(),
        library.isLast()
    );
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public PageResponse<UserLibraryResponse> findAllFavoriteGames(final int page, final int size,
      final Authentication connectedUser) throws AccessDeniedException {

    if (connectedUser == null || !connectedUser.isAuthenticated()) {
      throw new AccessDeniedException("User is not authenticated");
    }

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Pageable pageable = PageRequest.of(page, size);
    Page<UserLibrary> favoriteGames = libraryRepository.findUserLibraryByUserAndFavoriteTrue(user, pageable);

    List<UserLibraryResponse> favoriteGamesResponse = favoriteGames.getContent()
        .stream()
        .sorted(Comparator.comparing(library -> library.getGame().getTitle()))
        .map(userLibrary -> libraryMapper.toUserLibraryResponse(user, userLibrary))
        .toList();

    return new PageResponse<>(
        favoriteGamesResponse,
        favoriteGames.getNumber(),
        favoriteGames.getSize(),
        favoriteGames.getTotalElements(),
        favoriteGames.getTotalPages(),
        favoriteGames.isFirst(),
        favoriteGames.isLast()
    );
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public PageResponse<UserLibraryResponse> getDownloadedGames(final int page, final int size,
      final Authentication connectedUser) throws AccessDeniedException {
    if (connectedUser == null || !connectedUser.isAuthenticated()) {
      throw new AccessDeniedException("User is not authenticated");
    }

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Pageable pageable = PageRequest.of(page, size);
    Page<UserLibrary> downloadedGames = libraryRepository.findUserLibraryByUserAndInstalledIsTrue(user, pageable);

    List<UserLibraryResponse> downloadedGamesResponse = downloadedGames.getContent()
        .stream()
        .sorted(Comparator.comparing(library -> library.getGame().getTitle()))
        .map(userLibrary -> libraryMapper.toUserLibraryResponse(user, userLibrary))
        .toList();

    return new PageResponse<>(
        downloadedGamesResponse,
        downloadedGames.getNumber(),
        downloadedGames.getSize(),
        downloadedGames.getTotalElements(),
        downloadedGames.getTotalPages(),
        downloadedGames.isFirst(),
        downloadedGames.isLast()
    );
  }

  @Override
  @Transactional
  @PreAuthorize("isAuthenticated()")
  public Long addGameToFavorites(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    UserLibrary library = libraryRepository
        .findByUserIdAndGameId(user.getId(), gameId)
        .orElseThrow(() -> new EntityNotFoundException("Game is not in library"));

    if (!library.isFavorite()) {
      library.setFavorite(true);
      libraryRepository.save(library);
    }

    return game.getId();
  }

  @Override
  @Transactional
  @PreAuthorize("isAuthenticated()")
  public Long removeGameFromFavorites(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    UserLibrary library = libraryRepository
        .findByUserIdAndGameId(user.getId(), gameId)
        .orElseThrow(() -> new EntityNotFoundException("Game with " + gameId + " is not in library"));

    if (library.isFavorite()) {
      library.setFavorite(false);
      libraryRepository.save(library);
    }

    return game.getId();
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public Boolean checkGameFavorite(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    return libraryRepository.existsByUserAndGameAndFavoriteTrue(user, game);
  }

  @Override
  public Long downloadGame(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    UserLibrary library = libraryRepository.findByUserIdAndGameId(user.getId(), gameId)
        .orElseThrow(() -> new EntityNotFoundException("Game with " + gameId + " is not in library"));

    if (!library.isInstalled()) {
      library.setInstalled(true);
      libraryRepository.save(library);
    }

    return game.getId();
  }

  @Override
  public Long uninstallGame(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    UserLibrary library = libraryRepository.findByUserIdAndGameId(user.getId(), gameId)
        .orElseThrow(() -> new EntityNotFoundException("Game with " + gameId + " is not in library"));

    if (library.isInstalled()) {
      library.setInstalled(false);
      libraryRepository.save(library);
    }

    return game.getId();
  }

  @Override
  public Boolean checkDownloadedGame(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    // return libraryRepository.existsByUserAndGameAndFavoriteTrue(user, game);
    return libraryRepository.existsByUserAndGameAndInstalledTrue(user, game);
  }

  @Override
  public Long recommendGame(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    if (!user.getRecommendationGames().contains(game)) {
      user.getRecommendationGames().add(game);
      userRepository.save(user);
    }

    return game.getId();
  }

  @Override
  public Long removeRecommendGame(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    if (user.getRecommendationGames().contains(game)) {
      user.getRecommendationGames().remove(game);
      userRepository.save(user);
    }

    return game.getId();
  }

  @Override
  public Boolean checkGameRecommended(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    return user.getRecommendationGames().contains(game);
  }

  @Override
  public List<UserLibraryResponse> searchLibraryGames(final String query, final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    return libraryRepository.findAllByUserAndGameTitleContainingIgnoreCase(user, query)
        .stream()
        .map(userLibrary -> libraryMapper.toUserLibraryResponse(user, userLibrary))
        .toList();
  }

  @Override
  public Long playGame(final Authentication connectedUser, final Long gameId) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    UserLibrary libraryGame = libraryRepository.findByUserIdAndGameId(user.getId(), game.getId())
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    libraryGame.setPlayedAt(LocalDateTime.now());
    user.setStatus(Status.PLAYING);
    user.setCurrentlyPlayingGame(game);

    userRepository.save(user);
    libraryRepository.save(libraryGame);
    return game.getId();
  }

  @Override
  public void stopPlayingGame(final Authentication connectedUser) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    user.setStatus(Status.ONLINE);
    user.setCurrentlyPlayingGame(null);
    userRepository.save(user);
  }

  @Override
  public UserLibraryResponse getCurrentlyPlayingGame(final Authentication connectedUser) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    Game game = user.getCurrentlyPlayingGame();

    if (game == null) {
      return null;
    }

    return libraryMapper.toCurrentlyPlayingGameResponse(game);
  }

  // WHEN IMPLEMENTING SEE FRIENDS ON PROFILE USE getCurrentlyPlayingGame with userId

  @Override
  public List<RecentGamesResponse> getLast3PlayedGames(final Authentication connectedUser) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    List<UserLibrary> top3Games = libraryRepository.findTop3ByUserAndPlayedAtIsNotNullOrderByPlayedAtDesc(user);

    return top3Games.stream().map(libraryMapper::toRecentGamesResponse).toList();
  }

  @Override
  public List<RecentGamesResponse> getLast3PlayedGames(final Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + userId));

    List<UserLibrary> top3Games = libraryRepository.findTop3ByUserAndPlayedAtIsNotNullOrderByPlayedAtDesc(user);

    return top3Games.stream().map(libraryMapper::toRecentGamesResponse).toList();
  }

}