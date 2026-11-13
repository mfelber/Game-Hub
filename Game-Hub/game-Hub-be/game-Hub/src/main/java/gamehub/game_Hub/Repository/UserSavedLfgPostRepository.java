package gamehub.game_Hub.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import gamehub.game_Hub.Module.User.LfgPost;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.UserSavedLfgPost;
import gamehub.game_Hub.Module.embededIds.SavedLfgPostId;
import gamehub.game_Hub.enums.Lfg.LfgStatus;

public interface UserSavedLfgPostRepository extends JpaRepository<UserSavedLfgPost, SavedLfgPostId>, JpaSpecificationExecutor<UserSavedLfgPost>{

  Optional <UserSavedLfgPost> findByUserAndLfgPost(User user, LfgPost lfgPost);

  Page<UserSavedLfgPost> findAllByUserAndLfgPost_LfgStatus(User user, LfgStatus lfgPostLfgStatus, Pageable pageable);

  Integer countByUserAndLfgPostLfgStatus(User user, LfgStatus lfgPostLfgStatus);

}
