package gamehub.game_Hub.ServiceImpl;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.WishlistMapper;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.Wishlist;
import gamehub.game_Hub.Repository.WishlistRepository;
import gamehub.game_Hub.Response.WishlistResponse;
import gamehub.game_Hub.Service.WishlistService;
import gamehub.game_Hub.specification.WishlistSpecification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

  private final WishlistRepository wishlistRepository;

  private final WishlistMapper wishlistMapper;

  @Override
  @PreAuthorize("isAuthenticated()")
  public PageResponse<WishlistResponse> getUserWishlist(final Authentication connectedUser, final int page,
      final int size, String genre, String operationSystem, Boolean discount, String sortBy
  )
      throws AccessDeniedException {

    User authUser = (User) connectedUser.getPrincipal();

    Sort sort = switch (sortBy) {
      case "nameAsc" -> Sort.by("game.title").ascending();
      case "nameDesc" -> Sort.by("game.title").descending();
      case "recentlyAdded" -> Sort.by("addedAt").descending();
      default -> Sort.unsorted();
    };

    Pageable pageable = PageRequest.of(page, size, sort);

    Specification<Wishlist> specification = Specification.allOf();

    specification = specification.and(WishlistSpecification.belongsToUser(authUser));

    if (genre != null && !genre.isBlank()) {
      specification = specification.and(WishlistSpecification.hasGenre(genre));
    }

    if (operationSystem != null && !operationSystem.isBlank()) {
      specification = specification.and(WishlistSpecification.hasOperationSystem(operationSystem));
    }

    if (Boolean.TRUE.equals(discount)) {
      specification = specification.and(WishlistSpecification.hasDiscount());
    }

    if ("priceAsc".equals(sortBy)) {
      specification = specification.and(WishlistSpecification.sortByEffectivePrice(true));
    }

    if ("priceDesc".equals(sortBy)) {
      specification = specification.and(WishlistSpecification.sortByEffectivePrice(false));
    }

    Page<Wishlist> wishlist = wishlistRepository.findAll(specification, pageable);

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