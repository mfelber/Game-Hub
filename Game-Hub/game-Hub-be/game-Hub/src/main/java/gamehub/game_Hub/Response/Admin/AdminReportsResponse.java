package gamehub.game_Hub.Response.Admin;

import java.time.LocalDateTime;

import gamehub.game_Hub.Module.Report.ReportReason;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.enums.ReportStatus;
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
public class AdminReportsResponse {

  private Long reportId;
  private Long reporterId;
  private String reporterUserName;
  private Long reportedUserId;
  private String reportedUserName;
  private String reportedUserEmail;
  private String reportReason;
  private ReportStatus reportStatus;
  private LocalDateTime createdAt;

}
