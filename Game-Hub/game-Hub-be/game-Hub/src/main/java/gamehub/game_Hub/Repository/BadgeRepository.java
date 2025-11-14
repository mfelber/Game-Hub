package gamehub.game_Hub.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Badge;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

  Badge findByName(String name);

}
