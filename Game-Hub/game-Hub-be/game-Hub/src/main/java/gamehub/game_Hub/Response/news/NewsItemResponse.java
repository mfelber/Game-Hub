package gamehub.game_Hub.Response.news;

import lombok.Builder;

@Builder
public record NewsItemResponse(
    String description
) {

}
