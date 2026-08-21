package gamehub.game_Hub.Mapper;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.Report.Report;
import gamehub.game_Hub.Response.Admin.AdminReportsResponse;
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

  public AdminReportsResponse toAdminReportResponse(Report report) {
    return AdminReportsResponse.builder()
        .reportId(report.getId())
        .reporterId(report.getReporterId().getId())
        .reporterUserName(report.getReporterId().getName())
        .reportedUserId(report.getReportedUserId().getId())
        .reportedUserName(report.getReportedUserId().getName())
        .reportedUserEmail(report.getReportedUserId().getEmail())
        .reportReason(report.getReason().getReason())
        .reportMsg(report.getMessage())
        .reportStatus(report.getStatus())
        .createdAt(report.getCreatedAt())
        .build();
  }

}
