package gamehub.game_Hub.Request.Settings;

import jakarta.validation.constraints.NotNull;

public record UpdateStoreSettingsRequest(

    @NotNull
    Long settingId,

    boolean disabled
) {

}
