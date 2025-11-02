package gamehub.game_Hub.ServiceImpl;

import java.util.List;

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
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.File.FileStorageService;
import gamehub.game_Hub.Request.GameRequest;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Response.PlatformResponse;
import gamehub.game_Hub.Service.GameService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

  private final GameRepository gameRepository;

  private final UserRepository userRepository;

  private final GameMapper gameMapper;

  private final FileStorageService fileStorageService;

  @Override
  public Long save(final GameRequest gameRequest) {
    Game game = gameMapper.toGame(gameRequest);
    return gameRepository.save(game).getId();
  }

  @Override
  public void uploadGameCoverImage(final Long gameId, final MultipartFile file) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("Game not found with id: " + gameId));
    var gameCoverImage = fileStorageService.saveFile(file);
    game.setGameCoverImage(gameCoverImage);
    gameRepository.save(game);
  }

  @Override
  public GameResponse findById(final Long gameId) {
    return gameRepository.findById(gameId)
        .map(gameMapper::toGameResponse)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
  }

  @Override
  public PageResponse<GameResponse> findAllGames(final int page, final int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    Page<Game> games = gameRepository.findAll(pageable);

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

    if (user.getWishlist().contains(game)) {
      user.getWishlist().remove(game);
      userRepository.save(user);
    }

    if (!user.getLibrary().contains(game)) {
      user.getLibrary().add(game);
      userRepository.save(user);
    }

    // TODO: Subtract the game price from the user's balance upon purchase

    return game.getId();
  }

  @Transactional
  public Long addGameToWishList(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    if (!user.getWishlist().contains(game)) {
      user.getWishlist().add(game);
      userRepository.save(user);
    }

    return game.getId();
  }

  @Override
  public Long removeGameToWishList(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    if (user.getWishlist().contains(game)) {
      user.getWishlist().remove(game);
      userRepository.save(user);
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

    return user.getLibrary().contains(game);
  }

  @Override
  public Boolean checkGameInWishlist(final Long gameId, final Authentication connectedUser) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("No game found with id: " + gameId));
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("No user found with id: " + authUser.getId()));

    return user.getWishlist().contains(game);
  }


}
