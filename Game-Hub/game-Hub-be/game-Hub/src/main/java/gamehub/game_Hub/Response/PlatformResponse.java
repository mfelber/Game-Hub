package gamehub.game_Hub.Response;

import lombok.Builder;

@Builder
public record PlatformResponse(
    String enumName,
    String name,
    String icon
) {

}
