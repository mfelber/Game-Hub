package gamehub.game_Hub.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.GameMapper;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Service.WishlistService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

  private final GameRepository gameRepository;

  private final GameMapper gameMapper;

  @Override
  @PreAuthorize("isAuthenticated()")
  public PageResponse<GameResponse> FindAllWishlistGames(final int page, final int size,
      final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();

    Pageable pageable = PageRequest.of(page, size);
    Page<Game> wishlist = gameRepository.findByWishlistsContaining(authUser, pageable);

    List<GameResponse> gameResponse = wishlist.stream().map(gameMapper::toGameResponse).toList();

    return new PageResponse<>(
        gameResponse,
        wishlist.getNumber(),
        wishlist.getSize(),
        wishlist.getTotalElements(),
        wishlist.getTotalPages(),
        wishlist.isFirst(),
        wishlist.isLast()
    );
  }

}