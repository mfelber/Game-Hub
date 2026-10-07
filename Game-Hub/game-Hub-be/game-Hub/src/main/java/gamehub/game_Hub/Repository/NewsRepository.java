package gamehub.game_Hub.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gamehub.game_Hub.Module.news.News;

public interface NewsRepository extends JpaRepository<News, Long> {

}
