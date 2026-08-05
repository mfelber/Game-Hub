package gamehub.game_Hub.Response.Admin;

import java.util.List;

import gamehub.game_Hub.Module.Report.Report;
import gamehub.game_Hub.Module.User.User;
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
public class DashboardResponse {

  private Long totalGames;
  private Long totalUsers;
  private Long pendingReports;
  private Long totalReviews;
  private List<User> recentUsers;
  private List<DashboardReportResponse> latestReports;
}
