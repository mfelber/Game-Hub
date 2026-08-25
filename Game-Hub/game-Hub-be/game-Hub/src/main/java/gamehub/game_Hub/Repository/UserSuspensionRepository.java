package gamehub.game_Hub.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserSuspensions;

public interface UserSuspensionRepository extends JpaRepository<UserSuspensions, Long> {

  @Query("""
    SELECT suspension
    FROM UserSuspensions suspension
    WHERE suspension.id IN (
        SELECT MAX(s.id)
        FROM UserSuspensions s
        GROUP BY s.userId
    )
    """)
  Page<UserSuspensions> findOnePerUser(Pageable pageable);

  Long countByUserId(User userId);

  Optional<UserSuspensions> findFirstByUserIdAndIdLessThanOrderByIdDesc(User userId, Long idIsLessThan);

}
