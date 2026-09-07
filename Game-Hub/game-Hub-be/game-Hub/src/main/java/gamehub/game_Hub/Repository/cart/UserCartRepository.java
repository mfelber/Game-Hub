package gamehub.game_Hub.Repository.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserCart;

public interface UserCartRepository extends JpaRepository<UserCart, Long> {

  Optional<UserCart> findByUser(User user);

  Long countByUser(User user);

}
