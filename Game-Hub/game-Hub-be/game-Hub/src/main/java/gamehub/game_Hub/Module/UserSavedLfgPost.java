package gamehub.game_Hub.Module;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import gamehub.game_Hub.Module.User.LfgPost;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.embededIds.SavedLfgPostId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
@Table(name = "user_saved_lfg_post", schema = "game_hub")
public class UserSavedLfgPost {

  @EmbeddedId
  private SavedLfgPostId id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("lfgPostId")
  @JoinColumn(name = "lfg_post_id")
  private LfgPost lfgPost;

  @CreationTimestamp
  @Column(name = "saved_at", nullable = false, updatable = false)
  private LocalDateTime savedAt;

}
