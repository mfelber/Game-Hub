package gamehub.game_Hub.ServiceImpl;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Mapper.NewsMapper;
import gamehub.game_Hub.Module.news.News;
import gamehub.game_Hub.Repository.NewsRepository;
import gamehub.game_Hub.Request.news.NewsRequest;
import gamehub.game_Hub.Service.NewsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

  private final NewsMapper newsMapper;

  private final NewsRepository newsRepository;

  @Override
  public Long createNews(final NewsRequest newsRequest) {
    News news = newsMapper.toNews(newsRequest);
    return newsRepository.save(news).getNewsId();
  }

}
