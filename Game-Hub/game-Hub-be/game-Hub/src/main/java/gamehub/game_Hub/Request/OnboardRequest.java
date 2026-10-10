package gamehub.game_Hub.Request;

import java.util.Set;

import gamehub.game_Hub.enums.MicrophoneUsage;
import gamehub.game_Hub.enums.Platform;
import jakarta.validation.constraints.NotNull;

public record OnboardRequest(
    @NotNull
    Platform mainPlatform,

    @NotNull
    MicrophoneUsage microphoneUsage,

    Set<Long> genreIds
) {

}
