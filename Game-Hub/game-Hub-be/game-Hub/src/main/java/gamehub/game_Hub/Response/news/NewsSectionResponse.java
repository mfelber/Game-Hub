package gamehub.game_Hub.Response.news;

import java.util.List;

import gamehub.game_Hub.enums.NewsType;
import lombok.Builder;

@Builder
public record NewsSectionResponse(
    NewsType newsType,
    String newsTypeName,
    String newsTypeIcon,
    String iconColor,
    List<NewsItemResponse> newsItems
) {



}
