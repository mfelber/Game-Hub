package gamehub.game_Hub.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.BanHistory;

public interface BanHistoryRepository extends JpaRepository<BanHistory, Long> {

  List<BanHistory> findByUserId(Long userId);

}
