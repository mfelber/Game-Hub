package gamehub.game_Hub.Module;

import java.time.LocalDateTime;

import gamehub.game_Hub.Module.User.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_library", schema = "game_hub")
public class UserLibrary {

  @EmbeddedId
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UserGameId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("gameId")
  @JoinColumn(name = "game_id")
  private Game game;

  private boolean installed;

  @Column(name = "playtime_minutes")
  private Integer playtimeMinutes;

  @Column(name = "last_played")
  private LocalDateTime lastPlayed;

  private boolean favorite;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

}
