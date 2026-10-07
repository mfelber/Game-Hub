package gamehub.game_Hub.Module.news;

import java.util.List;

import gamehub.game_Hub.enums.NewsType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "news_section", schema = "game_hub")
public class NewsSection {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "news_section_id")
  private Long newsSectionId;

  @Enumerated(EnumType.STRING)
  @Column(name = "news_type", nullable = false)
  private NewsType newsType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "news_id", nullable = false)
  private News news;

  @OneToMany(
      mappedBy = "newsSection",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<NewsItem> newsItemList;

}
