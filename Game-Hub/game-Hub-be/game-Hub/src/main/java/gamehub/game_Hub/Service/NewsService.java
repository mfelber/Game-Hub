package gamehub.game_Hub.Service;

import gamehub.game_Hub.Request.news.NewsRequest;
import jakarta.validation.Valid;

public interface NewsService {

  Long createNews(@Valid NewsRequest newsRequest);

}
