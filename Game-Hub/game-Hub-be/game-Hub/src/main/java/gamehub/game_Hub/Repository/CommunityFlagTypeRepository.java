package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.Flags.CommunityFlagType;

public interface CommunityFlagTypeRepository extends JpaRepository<CommunityFlagType, Integer> {

}
