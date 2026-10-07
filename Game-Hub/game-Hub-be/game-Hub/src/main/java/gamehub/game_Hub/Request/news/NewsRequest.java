package gamehub.game_Hub.Request.news;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewsRequest(

    @NotBlank
    String version,

    @NotBlank
    String title,

    List<NewsSectionRequest> sections

    ) {

}
