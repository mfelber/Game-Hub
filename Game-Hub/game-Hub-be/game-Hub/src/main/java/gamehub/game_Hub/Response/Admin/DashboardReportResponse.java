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
public class DashboardReportResponse {

  private Long id;
  private String reason;
  private String reportedUser;
  private LocalDateTime createdAt;

}
