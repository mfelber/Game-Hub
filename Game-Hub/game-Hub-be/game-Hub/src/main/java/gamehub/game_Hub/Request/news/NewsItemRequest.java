package gamehub.game_Hub.Request.news;

import jakarta.validation.constraints.NotBlank;

public record NewsItemRequest (
    @NotBlank
    String description
) {

}
