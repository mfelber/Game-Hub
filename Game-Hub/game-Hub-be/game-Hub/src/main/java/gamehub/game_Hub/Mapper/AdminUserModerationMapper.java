package gamehub.game_Hub.Mapper;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.BanHistory;
import gamehub.game_Hub.Module.Report.Report;
import gamehub.game_Hub.Module.User.UserWarnings;
import gamehub.game_Hub.Response.Admin.AdminBanResponse;
import gamehub.game_Hub.Response.Admin.AdminReportsResponse;
import gamehub.game_Hub.Response.Admin.AdminWarningResponse;

@Service
public class AdminUserModerationMapper {

  public AdminReportsResponse toAdminReportResponse(Report report) {
    return AdminReportsResponse.builder()
        .reportId(report.getId())
        .reporterId(report.getReporterId().getId())
        .reporterUserName(report.getReporterId().getName())
        .reportedUserId(report.getReportedUserId().getId())
        .reportedUserName(report.getReportedUserId().getName())
        .reportedUserEmail(report.getReportedUserId().getEmail())
        .reportReason(report.getReason().getCommunityGuideline())
        .reportMsg(report.getMessage())
        .reportStatus(report.getStatus())
        .moderationAction(report.getModerationAction())
        .createdAt(report.getCreatedAt())
        .build();
  }

  public AdminWarningResponse toAdminWarningResponse(UserWarnings warning) {
    return AdminWarningResponse.builder()
        .warningId(warning.getId())
        .userId(warning.getUser().getId())
        .reportId(warning.getReportId().getId())
        .adminMsg(warning.getMsgFromAdmin())
        .createdAt(warning.getCreatedAt())
        .build();
  }

  public AdminBanResponse toAdminBanResponse(BanHistory ban) {
    return AdminBanResponse.builder()
        .id(ban.getId())
        .userId(ban.getUser().getId())
        .reasonId(ban.getReason().getId())
        .banReason(ban.getReason().getCommunityGuideline())
        .reportId(ban.getReport() != null ? ban.getReport().getId() : null)
        .customMsg(ban.getCustomMsg())
        .bannedAt(ban.getBannedAt())
        .build();
  }

}
