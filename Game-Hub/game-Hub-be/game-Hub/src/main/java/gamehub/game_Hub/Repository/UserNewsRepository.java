package gamehub.game_Hub.Repository;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserNews;
import gamehub.game_Hub.Module.news.News;

public interface UserNewsRepository extends JpaRepository<UserNews, Long> {

  boolean existsByUserAndNews(User user, News news);

}
