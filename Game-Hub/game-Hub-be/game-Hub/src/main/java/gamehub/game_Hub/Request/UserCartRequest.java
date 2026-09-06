package gamehub.game_Hub.Request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserCartRequest(
    @NotNull
    Long gameId
) {

}
