package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.userSettings.PrivacySettingsDefinition;

public interface PrivacySettingsDefinitionRepository extends JpaRepository<PrivacySettingsDefinition, Long> {

}
