package gamehub.game_Hub.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.UserGameId;
import gamehub.game_Hub.Module.UserLibrary;

public interface UserLibraryRepository extends JpaRepository<UserLibrary, UserGameId> {

  Page<UserLibrary> findUserLibrariesByUser(User user, Pageable pageable);

  boolean existsUserLibrariesByUserAndGame(User user, Game game);

}
