package gamehub.game_Hub.Repository.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.User.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  Page<User> findAllByEmailIsNot(String email, Pageable pageable);

  Page<User> findAllByEmailIsNotAndUsername(String email, String username, Pageable pageable);

  boolean existsByIdAndFriends_Id(Long id, Long friendsId);

}
