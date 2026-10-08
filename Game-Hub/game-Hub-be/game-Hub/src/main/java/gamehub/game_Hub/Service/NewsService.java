package gamehub.game_Hub.Service;

import java.util.List;

import gamehub.game_Hub.Request.news.NewsRequest;
import gamehub.game_Hub.Response.news.NewsResponse;
import jakarta.validation.Valid;

public interface NewsService {

  Long createNews(@Valid NewsRequest newsRequest);

  List<NewsResponse> getNews();

}
