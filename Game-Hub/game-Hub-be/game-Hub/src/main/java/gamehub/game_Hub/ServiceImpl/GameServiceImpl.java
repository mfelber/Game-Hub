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
import gamehub.game_Hub.Repository.BadgeRepository;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.File.FileStorageService;
import gamehub.game_Hub.Request.GameRequest;
import gamehub.game_Hub.Response.GameResponse;
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

  private final BadgeRepository badgeRepository;

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

    addGameCollectorBadge(user);

    if (!user.getLibrary().contains(game)) {
      user.getLibrary().add(game);
      userRepository.save(user);
    }

    // TODO: Subtract the game price from the user's balance upon purchase

    return game.getId();
  }


  public void addGameCollectorBadge(User user) {

    int gameLibrarySize = user.getLibrary().size();
    System.out.println(gameLibrarySize);

    if (gameLibrarySize >= 4) {
      if (!hasBadge(user,"GAME_COLLECTOR_LEVEL_1")) {
        user.getBadges().add(badgeRepository.findByName("GAME_COLLECTOR_LEVEL_1"));
        Long xpReward = badgeRepository.findByName("GAME_COLLECTOR_LEVEL_1").getXpReward();
        Long userxp = user.getXp() + xpReward;
        user.setXp(userxp);
      }
    }
    if (gameLibrarySize >= 16) {

      if (hasBadge(user,"GAME_COLLECTOR_LEVEL_1")) {
        user.getBadges().remove(badgeRepository.findByName("GAME_COLLECTOR_LEVEL_1"));
      }
      if (!hasBadge(user,"GAME_COLLECTOR_LEVEL_2")) {
        user.getBadges().add(badgeRepository.findByName("GAME_COLLECTOR_LEVEL_2"));
        Long xpReward = badgeRepository.findByName("GAME_COLLECTOR_LEVEL_2").getXpReward();
        Long userxp = user.getXp() + xpReward;
        user.setXp(userxp);
      }
    }

    if (gameLibrarySize >= 31) {
      if (hasBadge(user,"GAME_COLLECTOR_LEVEL_2")) {
        user.getBadges().remove(badgeRepository.findByName("GAME_COLLECTOR_LEVEL_2"));
      }
      if (!hasBadge(user,"GAME_COLLECTOR_LEVEL_3")) {
        user.getBadges().add(badgeRepository.findByName("GAME_COLLECTOR_LEVEL_3"));
        Long xpReward = badgeRepository.findByName("GAME_COLLECTOR_LEVEL_3").getXpReward();
        Long userxp = user.getXp() + xpReward;
        user.setXp(userxp);
      }
    }

    if (gameLibrarySize >= 51) {
      if (hasBadge(user,"GAME_COLLECTOR_LEVEL_3")) {
        user.getBadges().remove(badgeRepository.findByName("GAME_COLLECTOR_LEVEL_3"));
      }
      if (!hasBadge(user,"GAME_COLLECTOR_LEVEL_4")) {
        user.getBadges().add(badgeRepository.findByName("GAME_COLLECTOR_LEVEL_4"));
        Long xpReward = badgeRepository.findByName("GAME_COLLECTOR_LEVEL_4").getXpReward();
        Long userxp = user.getXp() + xpReward;
        user.setXp(userxp);
      }
    }

    if (gameLibrarySize >= 101) {
      if (hasBadge(user,"GAME_COLLECTOR_LEVEL_4")) {
        user.getBadges().remove(badgeRepository.findByName("GAME_COLLECTOR_LEVEL_4"));
      }
      if (!hasBadge(user,"GAME_COLLECTOR_LEVEL_5")) {
        user.getBadges().add(badgeRepository.findByName("GAME_COLLECTOR_LEVEL_5"));
        Long xpReward = badgeRepository.findByName("GAME_COLLECTOR_LEVEL_5").getXpReward();
        Long userxp = user.getXp() + xpReward;
        user.setXp(userxp);
      }
    }
    
  }

  private boolean hasBadge(final User user, String badgeName) {
    return user.getBadges().contains(badgeRepository.findByName(badgeName));

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