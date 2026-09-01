package gamehub.game_Hub.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.BanHistory;
import gamehub.game_Hub.Module.User.User;

public interface BanHistoryRepository extends JpaRepository<BanHistory, Long> {

  List<BanHistory> findByUserId(Long userId);

  Optional<BanHistory> findFirstByUserOrderByBannedAtDesc(User user);

}
