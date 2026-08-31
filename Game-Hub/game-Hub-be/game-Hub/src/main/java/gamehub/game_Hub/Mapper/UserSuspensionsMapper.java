package gamehub.game_Hub.Mapper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import gamehub.game_Hub.Module.BanHistory;
import gamehub.game_Hub.Module.Report.Report;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.User.UserSuspensions;
import gamehub.game_Hub.Module.User.UserWarnings;
import gamehub.game_Hub.Repository.UserSuspensionRepository;
import gamehub.game_Hub.Response.Admin.AdminSuspendedAccountsResponse;
import gamehub.game_Hub.Response.Admin.AdminUserModerationResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSuspensionsMapper {

  private final UserSuspensionRepository userSuspensionRepository;

  private final AdminUserModerationMapper adminUserModerationMapper;

  public AdminSuspendedAccountsResponse toUserSuspensionResponse(UserSuspensions userSuspension) {

    Long suspendedTimes = userSuspensionRepository.countByUser(userSuspension.getUser());

    Optional<UserSuspensions> previousSuspension = userSuspensionRepository.findFirstByUserIdAndIdLessThanOrderByIdDesc(
        userSuspension.getUser().getId(), userSuspension.getId());

    LocalDate today = LocalDate.now();

    UserSuspensions lastSuspension = previousSuspension.orElse(userSuspension);

    LocalDate suspensionEnded = lastSuspension.getExpiresAt().toLocalDate();

    Long elapsedDays = ChronoUnit.DAYS.between(suspensionEnded, today);

    return AdminSuspendedAccountsResponse.builder()
        .suspensionId(userSuspension.getId())
        .reportId(userSuspension.getReport().getId())
        .userId(userSuspension.getUser().getId())
        .userName(userSuspension.getUser().getName())
        .suspensionReason(userSuspension.getSuspensionReason().getCommunityGuideline())
        .messageFromAdmin(userSuspension.getCustomMessage())
        .suspendedTimes(suspendedTimes)
        .daysSinceLastSuspension(elapsedDays)
        .createdAt(String.valueOf(userSuspension.getCreatedAt()))
        .expiresAt(String.valueOf(userSuspension.getExpiresAt()))
        .suspensionStatus(userSuspension.getSuspensionStatus())
        .build();
  }

  public AdminUserModerationResponse toAdminUserModerationResponse(final User user, final List<Report> reports,
      List<UserSuspensions> suspensions, List<UserWarnings> warnings, List<BanHistory> bans) {
    return AdminUserModerationResponse.builder()
        .userId(user.getId())
        .userName(user.getName())
        .accountStatus(user.getAccountStatus())
        .registeredAt(user.getCreatedAt())
        .reportCount((long) reports.size())
        .suspensionCount((long) suspensions.size())
        .warningCount((long) warnings.size())
        .banCount((long) bans.size())
        .reports(reports.stream().sorted(Comparator.comparing(Report::getCreatedAt).reversed()).map(adminUserModerationMapper::toAdminReportResponse).toList())
        .suspensions(suspensions.stream().sorted(Comparator.comparing(UserSuspensions::getCreatedAt).reversed()).map(this::toUserSuspensionResponse).toList())
        .warnings(warnings.stream().sorted(Comparator.comparing(UserWarnings::getCreatedAt).reversed()).map(adminUserModerationMapper::toAdminWarningResponse).toList())
        .bans(bans.stream().sorted(Comparator.comparing(BanHistory::getBannedAt).reversed()).map(adminUserModerationMapper::toAdminBanResponse).toList())
        .build();
  }

}
