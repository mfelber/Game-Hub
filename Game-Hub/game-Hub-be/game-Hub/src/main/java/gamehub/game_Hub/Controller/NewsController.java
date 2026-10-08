package gamehub.game_Hub.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Request.news.NewsRequest;
import gamehub.game_Hub.Response.news.NewsResponse;
import gamehub.game_Hub.Service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("news")
@RequiredArgsConstructor
public class NewsController {

  private final NewsService newsService;

  // Only admin can add news
  @PostMapping("/news")
  public ResponseEntity<Long> createNews(@RequestBody @Valid NewsRequest newsRequest) {
    return ResponseEntity.ok(newsService.createNews(newsRequest));
  }

  @GetMapping("news")
  public ResponseEntity<List<NewsResponse>> getNews() {
    return ResponseEntity.ok(newsService.getNews());
  }


}
