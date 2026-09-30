package gamehub.game_Hub.Request.Settings;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserSettingsUpdateRequests(

    @NotNull
    List<UpdateCommunitySettingsRequest> communitySettingsIds,

    @NotNull
    List<UpdatePrivacySettingsRequest> privacySettingsIds,

    @NotNull
    List<UpdateStoreSettingsRequest> storeSettingsIds
    ) {

}

