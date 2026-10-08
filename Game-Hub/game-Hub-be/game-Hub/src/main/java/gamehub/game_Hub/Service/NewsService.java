package gamehub.game_Hub.Service;

import java.util.List;

import org.springframework.security.core.Authentication;

import gamehub.game_Hub.Request.news.NewsRequest;
import gamehub.game_Hub.Response.NewsOverviewResponse;
import gamehub.game_Hub.Response.news.NewsResponse;
import jakarta.validation.Valid;

public interface NewsService {

  Long createNews(@Valid NewsRequest newsRequest);

  NewsOverviewResponse getNews(Authentication connectedUser);

  void markNewsAsSeen(Long newsId, Authentication connectedUser);

}
