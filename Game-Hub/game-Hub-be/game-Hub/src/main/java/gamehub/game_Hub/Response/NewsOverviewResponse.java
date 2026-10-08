package gamehub.game_Hub.Response;

import java.util.List;

import gamehub.game_Hub.Response.news.NewsResponse;
import lombok.Builder;

@Builder
public record NewsOverviewResponse(
    List<NewsResponse> news,
    boolean hasUnseenNews
){

}
