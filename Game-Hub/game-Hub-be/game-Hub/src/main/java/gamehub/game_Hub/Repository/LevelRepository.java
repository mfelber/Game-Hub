package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Level;

public interface LevelRepository extends JpaRepository<Level, Long> {

}
