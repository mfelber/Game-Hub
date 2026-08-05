package gamehub.game_Hub.Mapper;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.Report.Report;
import gamehub.game_Hub.Response.Admin.DashboardReportResponse;

@Service
public class ReportMapper {

  public DashboardReportResponse toReportResponse(Report report) {
    return DashboardReportResponse.builder()
        .id(report.getId())
        .reportedUser(report.getReportedUserId().getName())
        .reason(report.getReason().getReason())
        .createdAt(report.getCreatedAt())
        .build();
  }

}
