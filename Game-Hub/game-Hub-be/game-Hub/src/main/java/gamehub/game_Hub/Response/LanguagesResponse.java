package gamehub.game_Hub.Response;

import lombok.Builder;

@Builder
public record LanguagesResponse(
    String name,
    String label
) {

}
