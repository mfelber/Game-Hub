package gamehub.game_Hub.Request.Settings;

import gamehub.game_Hub.Module.userSettings.Access;
import jakarta.validation.constraints.NotNull;

public record UpdatePrivacySettingsRequest(

    @NotNull
    Long settingId,

    @NotNull
    Access access
) {

}
