package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

}
