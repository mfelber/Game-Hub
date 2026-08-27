package gamehub.game_Hub.Response.Admin;

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
public class AdminWarningResponse {

  private Long warningId;
  private Long userId;
  private Long reportId;
  private String adminMsg;
  private LocalDateTime createdAt;


}
