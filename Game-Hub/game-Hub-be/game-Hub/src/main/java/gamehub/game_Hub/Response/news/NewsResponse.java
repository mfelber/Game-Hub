package gamehub.game_Hub.Response.news;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record NewsResponse (
    @NotNull
    Long newsId,
    String version,
    String title,
    LocalDateTime createdAt,
    List<NewsSectionResponse> newsSections
) {

}
