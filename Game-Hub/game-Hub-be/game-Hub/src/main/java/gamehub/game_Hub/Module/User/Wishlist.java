package gamehub.game_Hub.Module.User;

import java.time.LocalDateTime;

import gamehub.game_Hub.Module.Game;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wishlist", schema = "game_hub")
public class Wishlist {

  @EmbeddedId
  private UserWishlistId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("gameId")
  @JoinColumn(name = "game_id")
  private Game game;

  @Column(name = "added_at")
  private LocalDateTime addedAt;



}
