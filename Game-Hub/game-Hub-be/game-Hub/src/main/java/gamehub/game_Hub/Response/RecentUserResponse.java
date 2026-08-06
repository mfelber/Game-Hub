package gamehub.game_Hub.Response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentUserResponse {
  private Long id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private LocalDateTime createdAt;
}
