package gamehub.game_Hub.specification;

import jakarta.persistence.criteria.Expression;

import org.springframework.data.jpa.domain.Specification;

import gamehub.game_Hub.Module.Game;

public class StoreSpecification {

  public static Specification<Game> hasGenre(String genre) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.join("genres").get("name"),
            genre
        );
  }

  public static Specification<Game> hasDiscount() {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.isTrue(root.get("hasDiscount"));
  }

  public static Specification<Game> hasOperationSystem(String operationSystem) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.join("platforms").get("name"),
            operationSystem
        );
  }

  public static Specification<Game> priceGreaterThanOrEqual(Double minPrice) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.get("price"),
            minPrice
        );
  }

  public static Specification<Game> priceLessThanOrEqual(Double maxPrice) {
    return (root, query, criteriaBuilder) -> {

      Expression<Double> effectivePrice = criteriaBuilder
          .<Double>selectCase()
          .when(
              criteriaBuilder.isTrue(root.get("hasDiscount")),
              root.get("discountPrice")
          )
          .otherwise(root.get("price"));

      return criteriaBuilder.lessThanOrEqualTo(
          effectivePrice,
          maxPrice
      );
    };
  }

}
