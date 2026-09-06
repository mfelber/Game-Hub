package gamehub.game_Hub.specification;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Expression;

import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.Wishlist;

public class WishlistSpecification {

  public static Specification<Wishlist> belongsToUser(User user) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("user"),
            user
        );
  }

  public static Specification<Wishlist> hasGenre(String genre) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.join("game").join("genres").get("name"),
            genre
        );
  }

  public static Specification<Wishlist> hasDiscount() {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.isTrue(root.join("game").get("hasDiscount"));
  }

  public static Specification<Wishlist> hasOperationSystem(String operationSystem) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.join("game").join("platforms").get("name"),
            operationSystem
        );
  }

  public static Specification<Wishlist> sortByEffectivePrice(boolean ascending) {
    return (root, query, criteriaBuilder) -> {
      var game = root.join("game");
      Expression<Double> effectivePrice = criteriaBuilder.<Double>selectCase().when(criteriaBuilder.isTrue(game.get("hasDiscount")), game.get("discountPrice")).otherwise(
          game.get("price")
      );
      if (ascending) {
        query.orderBy(criteriaBuilder.asc(effectivePrice));
      } else {
        query.orderBy(criteriaBuilder.desc(effectivePrice));
      }
      return criteriaBuilder.conjunction();
    };
  }

}
