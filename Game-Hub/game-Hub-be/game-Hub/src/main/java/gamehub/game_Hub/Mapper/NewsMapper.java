package gamehub.game_Hub.Mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.news.News;
import gamehub.game_Hub.Module.news.NewsItem;
import gamehub.game_Hub.Module.news.NewsSection;
import gamehub.game_Hub.Request.news.NewsItemRequest;
import gamehub.game_Hub.Request.news.NewsRequest;
import gamehub.game_Hub.Request.news.NewsSectionRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsMapper {

  public News toNews(final NewsRequest newsRequest) {
    // Create news
    var news =  News.builder()
        .title(newsRequest.title())
        .version(newsRequest.version())
        .build();

    List<NewsSection> sectionList = newsRequest.sections().stream().map(this::toNewsSection).toList();
    sectionList.forEach(section -> section.setNews(news));
    news.setNewsSectionList(sectionList);
    return news;
  }

  public NewsSection toNewsSection(final NewsSectionRequest newsSectionRequest){
    var section = NewsSection.builder()
        .newsType(newsSectionRequest.newsType())
        .build();

    List<NewsItem> itemList = newsSectionRequest.newsItems().stream().map(this::toNewsItem).toList();

    itemList.forEach(item -> item.setNewsSection(section));
    section.setNewsItemList(itemList);
    return section;
  }

  public NewsItem toNewsItem(final NewsItemRequest newsItemRequest) {
    return NewsItem.builder()
        .description(newsItemRequest.description())
        .build();
  }

}
