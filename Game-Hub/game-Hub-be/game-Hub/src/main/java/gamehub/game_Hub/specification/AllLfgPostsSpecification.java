package gamehub.game_Hub.specification;

import org.springframework.data.jpa.domain.Specification;

import gamehub.game_Hub.Module.User.LfgPost;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.enums.Lfg.LfgStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class AllLfgPostsSpecification {

  public static Specification<LfgPost> authorIsNot(Long userId) {
    return (root, query, cb) -> cb.notEqual(root.get("author").get("id"), userId);
  }

  public static Specification<LfgPost> lfgPostStatus(LfgStatus status) {
    return ((root, query, cb) -> root.get("lfgStatus").in(status));
  }

  public static Specification<LfgPost> authorUsernameContains(String searchQuery) {
    return (root, query2, cb) ->
    {
      Join<LfgPost, User> author = root.join("author", JoinType.INNER);
      return cb.like(cb.lower(author.get("username")), "%" + searchQuery.toLowerCase() + "%");
    };
  }

  public static Specification<LfgPost> gameTitleContains(String searchQuery) {
    return (root, query2, cb) ->
    {
      Join<LfgPost, User> game = root.join("game", JoinType.INNER);
      return cb.like(cb.lower(game.get("title")), "%" + searchQuery.toLowerCase() + "%");
    };
  }


}
