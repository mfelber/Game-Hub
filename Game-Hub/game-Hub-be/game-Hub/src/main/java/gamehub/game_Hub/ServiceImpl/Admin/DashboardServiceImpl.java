package gamehub.game_Hub.ServiceImpl.Admin;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Mapper.ReportMapper;
import gamehub.game_Hub.Module.User.Role;
import gamehub.game_Hub.Repository.ReportRepository;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.Admin.DashboardResponse;
import gamehub.game_Hub.Service.Admin.DashboardService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

  private final GameRepository gameRepository;

  private final UserRepository userRepository;

  private final ReportRepository reportRepository;

  private final ReportMapper reportMapper;

  @Override
  public DashboardResponse loadDashboardData(final Authentication connectedUser, final int page,
      final int size) {
    // TODO change total reviews while implementing reviews
    return DashboardResponse.builder()
        .totalGames(gameRepository.count())
        .totalUsers(userRepository.count() - 1)
        .pendingReports(reportRepository.countReportsByStatus_Id(1L))
        .totalReviews(100L)
        .recentUsers(userRepository.findTop5ByRoleNotOrderByCreatedAtDesc(Role.ADMIN))
        .latestReports(reportRepository.findTop5ByOrderByCreatedAtDesc().stream().map(reportMapper::toReportResponse).toList())
        .build();
  }

}
