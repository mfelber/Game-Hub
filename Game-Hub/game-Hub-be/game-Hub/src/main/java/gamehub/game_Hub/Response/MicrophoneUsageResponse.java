package gamehub.game_Hub.Response;

import lombok.Builder;

@Builder
public record MicrophoneUsageResponse(
    String enumName,
    String name,
    String icon
) {

}
