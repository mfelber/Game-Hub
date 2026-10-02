package gamehub.game_Hub.Module.userSettings;

import gamehub.game_Hub.Module.User.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
@Table(name = "user_settings", schema = "game_hub")
public class UserSettings {

  @Id
  @Column(name = "user_id")
  private Long userId;

  @OneToOne
  @MapsId
  private User user;

}
