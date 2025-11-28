package gamehub.game_Hub.Repository.game;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.User;

public interface GameRepository extends JpaRepository<Game, Long> {

  Page<Game> findByOwnersContaining(User connectedUser, Pageable pageable);

  Page<Game> findByWishlistsContaining(User connectedUser, Pageable pageable);

  Page<Game> findByFavoriteGamesContaining(User connectedUser, Pageable pageable);

  Page<Game> findAllByAgeRating_AgeRatingNotIn(Collection<String> ageRatingAgeRatings, Pageable pageable);
}
