package gamehub.game_Hub.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Mapper.NewsMapper;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserNews;
import gamehub.game_Hub.Module.news.News;
import gamehub.game_Hub.Repository.NewsRepository;
import gamehub.game_Hub.Repository.UserNewsRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Request.news.NewsRequest;
import gamehub.game_Hub.Response.NewsOverviewResponse;
import gamehub.game_Hub.Response.news.NewsResponse;
import gamehub.game_Hub.Service.NewsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

  private final NewsMapper newsMapper;

  private final NewsRepository newsRepository;

  private final UserRepository userRepository;

  private final UserNewsRepository userNewsRepository;

  @Override
  public Long createNews(final NewsRequest newsRequest) {
    News news = newsMapper.toNews(newsRequest);
    return newsRepository.save(news).getNewsId();
  }

  @Override
  public NewsOverviewResponse getNews(Authentication connectedUser) {

    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + authUser.getId()));

    List<News> latestNews = newsRepository.findTop2ByOrderByCreatedAtDesc();
    boolean hasUnseenNews = latestNews.stream().anyMatch(news -> !userNewsRepository.existsByUserAndNews(user, news));

    List<NewsResponse> newsResponses = latestNews.stream().map(newsMapper::toNewsResponse).toList();
    // return latestNews.stream().map(newsMapper::toNewsResponse).toList();
    return NewsOverviewResponse.builder()
        .news(newsResponses)
        .hasUnseenNews(hasUnseenNews)
        .build();
  }

  @Override
  public void markNewsAsSeen(final Long newsId, final Authentication connectedUser) {
    User authUser = (User) connectedUser.getPrincipal();
    User user = userRepository.findById(authUser.getId())
        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + authUser.getId()));

    News news = newsRepository.findNewsByNewsId(newsId);
    UserNews userNews = UserNews.builder()
        .user(user)
        .news(news)
        .build();

    userNewsRepository.save(userNews);
  }

}
