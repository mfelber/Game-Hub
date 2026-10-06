package gamehub.game_Hub.Request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateBioRequest(
    @Size(max = 500)
    String bio
)

{

}
