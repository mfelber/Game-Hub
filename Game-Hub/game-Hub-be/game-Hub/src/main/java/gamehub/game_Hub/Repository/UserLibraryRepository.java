package gamehub.game_Hub.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserGameId;
import gamehub.game_Hub.Module.User.UserLibrary;

public interface UserLibraryRepository extends JpaRepository<UserLibrary, UserGameId> {

  Page<UserLibrary> findUserLibrariesByUser(User user, Pageable pageable);

  boolean existsUserLibrariesByUserAndGame(User user, Game game);

  Optional<UserLibrary> findByUserIdAndGameId(Long userId, Long gameId);

  Page<UserLibrary> findUserLibraryByUserAndFavoriteTrue(User user, Pageable pageable);

  Boolean existsByUserAndGameAndFavoriteTrue(User user, Game game);

  Page<UserLibrary> findUserLibraryByUserAndInstalledIsTrue(User user, Pageable pageable);

  Boolean existsByUserAndGameAndInstalledTrue(User user, Game game);

  void deleteAllByGame(Game game);

}
