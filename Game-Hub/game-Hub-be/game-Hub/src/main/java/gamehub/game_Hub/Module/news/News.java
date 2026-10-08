package gamehub.game_Hub.Module.news;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "news", schema = "game_hub")
public class News {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "news_id")
  private Long newsId;

  private String version;

  private String title;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<NewsSection> newsSectionList;

}
