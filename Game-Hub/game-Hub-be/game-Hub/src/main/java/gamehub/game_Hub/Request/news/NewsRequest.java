package gamehub.game_Hub.Request.news;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record NewsRequest(

    @NotBlank
    String version,

    @NotBlank
    String title,

    List<NewsSectionRequest> sections

    ) {

}
