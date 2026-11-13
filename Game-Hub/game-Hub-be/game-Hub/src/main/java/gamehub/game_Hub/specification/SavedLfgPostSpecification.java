package gamehub.game_Hub.specification;

import org.springframework.data.jpa.domain.Specification;

import gamehub.game_Hub.Module.User.LfgPost;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.UserSavedLfgPost;
import gamehub.game_Hub.enums.Lfg.LfgStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class SavedLfgPostSpecification {

  public static Specification<UserSavedLfgPost> savedByUser(Long userId) {
    return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
  }

  public static Specification<UserSavedLfgPost> lfgPostStatus(LfgStatus status) {
    return (root, query, cb) -> cb.equal(root.get("lfgPost").get("lfgStatus"), status);
  }

  public static Specification<UserSavedLfgPost> authorUsernameContains(String searchQuery) {
    return (root, query, cb) ->
    {
      Join<?, ?> author = root.join("lfgPost", JoinType.INNER).join("author", JoinType.INNER);
      return cb.like(cb.lower(author.get("username")), "%" + searchQuery.toLowerCase() + "%");
    };
  }

  public static Specification<UserSavedLfgPost> gameTitleContains(String searchQuery) {
    return (root, query, cb) -> {
      Join<?, ?> game = root.join("lfgPost", JoinType.INNER).join("game", JoinType.INNER);
      return cb.like(cb.lower(game.get("title")), "%" + searchQuery.toLowerCase() + "%");
    };
  }

}
