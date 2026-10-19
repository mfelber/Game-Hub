package gamehub.game_Hub.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.User.LfgPost;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.enums.Lfg.LfgStatus;

public interface LfgPostRepository extends JpaRepository<LfgPost, Long> {

  Page<LfgPost> findAllByLfgStatus(LfgStatus lfgStatus, Pageable pageable);

  Long countLfgPostByLfgStatusAndAuthor(LfgStatus lfgStatus, User user);

}
