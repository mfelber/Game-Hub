package gamehub.game_Hub.ServiceImpl;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.GameMapper;
import gamehub.game_Hub.Mapper.WishlistMapper;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.Wishlist;
import gamehub.game_Hub.Repository.WishlistRepository;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Response.WishlistResponse;
import gamehub.game_Hub.Service.WishlistService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

  private final WishlistRepository wishlistRepository;

  private final WishlistMapper wishlistMapper;

  @Override
  @PreAuthorize("isAuthenticated()")
  public PageResponse<WishlistResponse> getUserWishlist(final int page, final int size,
      final Authentication connectedUser)
      throws AccessDeniedException {
    User authUser = (User) connectedUser.getPrincipal();

    Pageable pageable = PageRequest.of(page, size);
    Page<Wishlist> wishlist = wishlistRepository.findWishlistByUser(authUser, pageable);

    List<WishlistResponse> wishlistResponse = wishlist.stream().map(wishlistMapper::toWishlistResponse).toList();

    return new PageResponse<>(
        wishlistResponse,
        wishlist.getNumber(),
        wishlist.getSize(),
        wishlist.getTotalElements(),
        wishlist.getTotalPages(),
        wishlist.isFirst(),
        wishlist.isLast()
    );
  }

}