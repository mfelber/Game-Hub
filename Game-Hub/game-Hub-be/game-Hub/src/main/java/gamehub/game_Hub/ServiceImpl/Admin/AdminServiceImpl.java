package gamehub.game_Hub.ServiceImpl.Admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gamehub.game_Hub.Common.PageResponse;
import gamehub.game_Hub.Email.EmailService;
import gamehub.game_Hub.Email.EmailTemplate;
import gamehub.game_Hub.Mapper.GameMapper;
import gamehub.game_Hub.Mapper.ReportMapper;
import gamehub.game_Hub.Mapper.UserMapper;
import gamehub.game_Hub.Module.BanHistory;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.Report.Report;
import gamehub.game_Hub.Module.Report.ReportReason;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Repository.BanHistoryRepository;
import gamehub.game_Hub.Repository.ReportReasonRepository;
import gamehub.game_Hub.Request.BanUserRequest;
import gamehub.game_Hub.Response.Admin.AccountStatusResponse;
import gamehub.game_Hub.Response.Admin.AdminReportsResponse;
import gamehub.game_Hub.Response.Admin.AdminUserResponse;
import gamehub.game_Hub.Response.Admin.ReportStatusResponse;
import gamehub.game_Hub.Response.Admin.RoleResponse;
import gamehub.game_Hub.Response.ReportReasonResponse;
import gamehub.game_Hub.enums.AccountStatus;
import gamehub.game_Hub.enums.ReportStatus;
import gamehub.game_Hub.enums.Role;
import gamehub.game_Hub.Repository.ReportRepository;
import gamehub.game_Hub.Repository.UserLibraryRepository;
import gamehub.game_Hub.Repository.WishlistRepository;
import gamehub.game_Hub.Repository.game.GameRepository;
import gamehub.game_Hub.Repository.user.UserRepository;
import gamehub.game_Hub.Response.Admin.DashboardResponse;
import gamehub.game_Hub.Response.GamePreviewResponse;
import gamehub.game_Hub.Response.GameResponse;
import gamehub.game_Hub.Service.Admin.AdminService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final GameRepository gameRepository;

  private final UserRepository userRepository;

  private final ReportRepository reportRepository;

  private final ReportMapper reportMapper;

  private final GameMapper gameMapper;

  private final UserMapper userMapper;

  private final WishlistRepository wishlistRepository;

  private final UserLibraryRepository userLibraryRepository;

  private final ReportReasonRepository reportReasonRepository;

  private final BanHistoryRepository banHistoryRepository;

  private final EmailService emailService;

  @Value("${application.mailing.frontend.login-url}")
  private String logInUrl;

  @Override
  public DashboardResponse loadDashboardData(final Authentication connectedUser, final int page, final int size) {

    Long reportCounts = reportRepository.countReportsByStatusIn(List.of(ReportStatus.NEW, ReportStatus.IN_REVIEW));

    // TODO change total reviews while implementing reviews
    return DashboardResponse.builder()
        .totalGames(gameRepository.count())
        .totalUsers(userRepository.countByRole(Role.USER))
        .pendingReports(reportCounts)
        .totalReviews(100L)
        .recentUsers(userRepository.findTop5ByRoleNotOrderByCreatedAtDesc(Role.ADMIN)
            .stream()
            .map(userMapper::toRecentUserResponse)
            .toList())
        .latestReports(
            reportRepository.findTop5ByOrderByCreatedAtDesc().stream().map(reportMapper::toReportResponse).toList())
        .build();
  }

  @Override
  public PageResponse<GamePreviewResponse> getAllGames(final int page, final int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    Page<Game> games = gameRepository.findAll(pageable);
    List<GamePreviewResponse> gamesResponse = games.stream().map(gameMapper::toGamePreviewResponse).toList();

    return new PageResponse<>(gamesResponse, games.getNumber(), games.getSize(), games.getTotalElements(),
        games.getTotalPages(), games.isFirst(), games.isLast());
  }

  @Override
  public GameResponse getGameInfo(final Long gameId) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameId + " was not found"));
    return gameMapper.toGameResponse(game);
  }

  @Override
  @Transactional
  public void deleteGame(final Long gameId) {
    Game game = gameRepository.findById(gameId)
        .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameId + " was not found"));
    wishlistRepository.deleteAllByGame(game);
    userLibraryRepository.deleteAllByGame(game);
    gameRepository.delete(game);
  }

  @Override
  public PageResponse<AdminUserResponse> getAllUsers(final int page, final int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
    Page<User> users = userRepository.findAll(pageable);
    List<AdminUserResponse> userResponse = users.stream().map(userMapper::toAdminUserResponse).toList();

    return new PageResponse<>(userResponse, users.getNumber(), users.getSize(), users.getTotalElements(),
        users.getTotalPages(), users.isFirst(), users.isLast());
  }

  @Override
  public List<RoleResponse> getAllRoles() {
    return Arrays.stream(Role.values()).map(role -> new RoleResponse(role.name())).collect(Collectors.toList());
  }

  @Override
  public List<AccountStatusResponse> getAllAccountStatuses() {
    return Arrays.stream(AccountStatus.values())
        .map(accountStatus -> new AccountStatusResponse(accountStatus.name()))
        .collect(Collectors.toList());
  }

  @Override
  public AdminUserResponse getUserInfo(final Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));
    return userMapper.toAdminUserResponse(user);
  }

  @Override
  public Long changeRole(final Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));

    if (user.getRole() == Role.ADMIN) {
      user.setRole(Role.USER);
    } else {
      user.setRole(Role.ADMIN);
    }

    return userRepository.save(user).getId();
  }

  @Override
  public Long banUser(final Long userId, BanUserRequest banUserRequest) throws MessagingException {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));

    ReportReason banReason = reportReasonRepository.findById(banUserRequest.getBanReason())
        .orElseThrow(() -> new EntityNotFoundException("No reason found with id: " + banUserRequest.getBanReason()));

    // TODO GH-182 increment counter bannedTimes++

    var banUser = BanHistory.builder()
        .user(user)
        .reason(banReason)
        .customMsg(banUserRequest.getCustomMessage())
        .build();

    user.setBanned(true);
    user.setAccountStatus(AccountStatus.BANNED);
    banHistoryRepository.save(banUser);

    sendBannedUserEmail(user);
    return userRepository.save(user).getId();
  }

  @Override
  public Long unBanUser(final Long userId) throws MessagingException {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));

    user.setBanned(false);
    user.setAccountStatus(AccountStatus.ACTIVE);
    sendAccountRestored(user);
    return userRepository.save(user).getId();
  }

  @Override
  public PageResponse<AdminReportsResponse> getAllReports(final int page, final int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<Report> reports = reportRepository.findAll(pageable);
    List<AdminReportsResponse> reportResponse = reports.stream().map(reportMapper::toAdminReportResponse).toList();

    return new PageResponse<>(reportResponse, reports.getNumber(), reports.getSize(), reports.getTotalElements(),
        reports.getTotalPages(), reports.isFirst(), reports.isLast());
  }

  @Override
  public List<ReportStatusResponse> getAllReportStatuses() {
    return Arrays.stream(ReportStatus.values())
        .map(status -> new ReportStatusResponse(status.name()))
        .toList();
  }

  private void sendAccountRestored(final User user) throws MessagingException {
    emailService.sendAccountRestored(user.getEmail(), user.getName(), EmailTemplate.USER_ACCOUNT_RESTORED_EMAIL,
        logInUrl, "Your GameHub account has been successfully restored");
  }

  private void sendBannedUserEmail(final User user) throws MessagingException {
    String reason = banHistoryRepository.findByUserId(user.getId())
        .stream()
        .findFirst()
        .map(banHistory -> banHistory.getReason().getReason())
        .orElse(null);

    String customMsg = banHistoryRepository.findByUserId(user.getId())
        .stream()
        .findFirst()
        .map(banHistory -> banHistory.getCustomMsg())
        .orElse(null);

    // TODO dont send user.getId() but send id of ban when implementing chat between user and admin
    // on fe show report id with # report.getId()
    String appealUrl = "http://localhost:4200/send-appeal?appeal=" + user.getId();
    emailService.sendBannedUserEmail(user.getEmail(), user.getName(), reason, customMsg,
        EmailTemplate.USER_BANNED_EMAIL, appealUrl, "Your GameHub account has been banned — Appeal available");
  }

}
