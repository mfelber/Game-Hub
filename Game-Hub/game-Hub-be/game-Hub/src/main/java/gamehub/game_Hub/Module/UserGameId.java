package gamehub.game_Hub.Module;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGameId implements Serializable {

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "game_id")
  private Long gameId;

}
