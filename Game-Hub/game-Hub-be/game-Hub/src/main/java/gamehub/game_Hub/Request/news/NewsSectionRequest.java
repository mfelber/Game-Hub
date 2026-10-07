package gamehub.game_Hub.Request.news;

import java.util.List;

import gamehub.game_Hub.enums.NewsType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewsSectionRequest(
    @NotNull
    NewsType newsType,

    @NotEmpty
    List<NewsItemRequest> newsItems
) {

}
