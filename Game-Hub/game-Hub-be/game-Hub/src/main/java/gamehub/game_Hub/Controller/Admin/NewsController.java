package gamehub.game_Hub.Controller.Admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gamehub.game_Hub.Request.news.NewsRequest;
import gamehub.game_Hub.Service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("news")
@RequiredArgsConstructor
public class NewsController {

  private final NewsService newsService;

  @PostMapping("/news")
  public ResponseEntity<Long> createNews(@RequestBody @Valid NewsRequest newsRequest) {
    return ResponseEntity.ok(newsService.createNews(newsRequest));
  }


}
