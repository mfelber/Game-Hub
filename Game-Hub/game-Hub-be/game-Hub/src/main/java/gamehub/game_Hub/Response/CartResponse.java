package gamehub.game_Hub.Response;

import java.util.List;

import lombok.Builder;

@Builder
public record CartResponse(
    Long cartId,
    List<GameResponse> games
) {
}
