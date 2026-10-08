package gamehub.game_Hub.Response.news;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record NewsResponse (
    String version,
    String title,
    LocalDateTime createdAt,
    List<NewsSectionResponse> newsSections
) {

}
