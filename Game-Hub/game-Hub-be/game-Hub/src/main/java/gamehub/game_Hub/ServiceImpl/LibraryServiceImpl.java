package gamehub.game_Hub.ServiceImpl;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.GameMapper;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Service.LibraryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

  private UserRepository userRepository;

  private final GameMapper gameMapper;

  private final GameRepository gameRepository;

  @Override
  public PageResponse<GameResponse> FindAllOwnedGames(final int page, final int size,
      final Authentication connectedUser) throws AccessDeniedException {

    if (connectedUser == null || !connectedUser.isAuthenticated()) {
      throw new AccessDeniedException("User is not authenticated");
    }

    User authUser = (User) connectedUser.getPrincipal();
    Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
    Page<Game> library = gameRepository.findByOwnersContaining(authUser,pageable);
    System.out.println(library.stream().map(game -> game.getTitle()).collect(Collectors.joining(",")));

    List<GameResponse> gameResponse = library.stream().map(gameMapper::toGameResponse).toList();

    return new PageResponse<>(
        gameResponse,
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

}
