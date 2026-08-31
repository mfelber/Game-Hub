package gamehub.game_Hub.Repository.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.enums.AccountStatus;
import gamehub.game_Hub.enums.Role;
import gamehub.game_Hub.Module.User.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByIdAndFriends_Id(Long id, Long friendsId);

  Page<User> findAllByEmailIsNotAndUsernameContainingIgnoreCase(String email, String username, Pageable pageable);

  List<User> findTop5ByRoleNotOrderByCreatedAtDesc(Role role);

  Long countByRole(Role role);

  Page<User> findAllByEmailIsNotAndRoleAndAccountStatusIn(String email, Role role, Collection<AccountStatus> accountStatuses, Pageable pageable);

}
