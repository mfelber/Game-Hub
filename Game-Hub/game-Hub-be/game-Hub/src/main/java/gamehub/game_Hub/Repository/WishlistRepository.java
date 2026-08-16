package gamehub.game_Hub.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserWishlistId;
import gamehub.game_Hub.Module.User.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, UserWishlistId> {

  Page<Wishlist> findWishlistByUser(User user, Pageable pageable);

  boolean existsByUserAndGame(User user, Game game);

  void deleteByUserAndGame(User user, Game game);

  void deleteAllByGame(Game game);

}
