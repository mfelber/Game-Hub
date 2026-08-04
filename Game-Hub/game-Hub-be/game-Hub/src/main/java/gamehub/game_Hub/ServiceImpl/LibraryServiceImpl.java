package gamehub.game_Hub.ServiceImpl;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.GameMapper;
import gamehub.game_Hub.Mapper.LibraryMapper;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserLibrary;
import gamehub.game_Hub.Repository.UserLibraryRepository;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Response.UserLibraryResponse;
import gamehub.game_Hub.Service.LibraryService;
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
    Pageable pageable = PageRequest.of(page, size, Sort.by("game").ascending());
    Page<UserLibrary> library = libraryRepository.findUserLibrariesByUser(authUser,pageable);

    List<UserLibraryResponse> libraryResponse = library.stream().map(libraryMapper::toUserLibraryResponse).toList();

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
  public Long addGameToFavorites(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    if (!user.getFavoriteGames().contains(game)) {
      user.getFavoriteGames().add(game);
      userRepository.save(user);
    }

    return game.getId();
  }

  @Override
  public Long removeGameFromFavorites(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    if (user.getFavoriteGames().contains(game)) {
      user.getFavoriteGames().remove(game);
      userRepository.save(user);
    }

    return game.getId();
  }

  @Override
  public Boolean checkGameFavorite(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    return user.getFavoriteGames().contains(game);
  }

  @Override
  public PageResponse<GameResponse> findAllFavoriteGames(final int page, final int size,
      final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
    Page<Game> favoriteGames = gameRepository.findByFavoriteGamesContaining(authUser,pageable);

    List<GameResponse> gameResponse = favoriteGames.stream().map(gameMapper::toGameResponse).toList();

    return new PageResponse<>(
        gameResponse,
        favoriteGames.getNumber(),
        favoriteGames.getSize(),
        favoriteGames.getTotalElements(),
        favoriteGames.getTotalPages(),
        favoriteGames.isFirst(),
        favoriteGames.isLast()
    );
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

}