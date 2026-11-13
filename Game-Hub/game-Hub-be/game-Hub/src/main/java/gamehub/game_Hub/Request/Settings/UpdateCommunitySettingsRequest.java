package gamehub.game_Hub.Request.Settings;

import gamehub.game_Hub.enums.user.Access;
import jakarta.validation.constraints.NotNull;

public record UpdateCommunitySettingsRequest(

    @NotNull
    Long settingId,

    @NotNull
    Access access
) {

}
