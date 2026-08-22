package gamehub.game_Hub.Module;

import java.util.ArrayList;
import java.util.List;

import gamehub.game_Hub.Module.Report.CommunityGuidelines;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "community_guideline_category", schema = "game_hub")
public class CommunityGuidelineCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "category_name")
  private String categoryName;

  @OneToMany(mappedBy = "category")
  private List<CommunityGuidelines> guidelines = new ArrayList<>();



}
