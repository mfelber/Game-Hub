package gamehub.game_Hub.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import gamehub.game_Hub.Module.User.LfgPost;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.enums.Lfg.LfgStatus;

public interface LfgPostRepository extends JpaRepository<LfgPost, Long>, JpaSpecificationExecutor<LfgPost> {

  Page<LfgPost> findAllByLfgStatus(LfgStatus lfgStatus, Pageable pageable);

  Long countLfgPostByLfgStatusAndAuthor(LfgStatus lfgStatus, User user);

  Optional<LfgPost> findByAuthorAndId(User author, Long id);

  List<LfgPost> findAllByAuthorAndLfgStatus(User author, LfgStatus lfgStatus);

}
