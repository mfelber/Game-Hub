package gamehub.game_Hub.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.User.LfgPost;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.UserSavedLfgPost;
import gamehub.game_Hub.Module.embededIds.SavedLfgPostId;

public interface UserSavedLfgPostRepository extends JpaRepository<UserSavedLfgPost, SavedLfgPostId> {

  Optional <UserSavedLfgPost> findByUserAndLfgPost(User user, LfgPost lfgPost);

}
