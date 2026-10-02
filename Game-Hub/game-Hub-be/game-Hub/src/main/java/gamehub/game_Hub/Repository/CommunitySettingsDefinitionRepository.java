package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.userSettings.CommunitySettingsDefinition;

public interface CommunitySettingsDefinitionRepository extends JpaRepository<CommunitySettingsDefinition, Long> {

}
