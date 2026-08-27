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
public class AdminBanResponse {

  private Long id;
  private Long userId;
  private Long reasonId;
  private String banReason;
  private Long reportId;
  private String customMsg;
  private LocalDateTime bannedAt;

}
