package gamehub.game_Hub.ServiceImpl.Admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
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
import gamehub.game_Hub.Mapper.UserSuspensionsMapper;
import gamehub.game_Hub.Module.BanHistory;
import gamehub.game_Hub.Module.Game;
import gamehub.game_Hub.Module.Report.CommunityGuidelines;
import gamehub.game_Hub.Module.Report.Report;
import gamehub.game_Hub.Module.User.User;
import gamehub.game_Hub.Module.UserSuspensions;
import gamehub.game_Hub.Repository.BanHistoryRepository;
import gamehub.game_Hub.Repository.CommunityGuidelinesRepository;
import gamehub.game_Hub.Repository.UserSuspensionRepository;
import gamehub.game_Hub.Request.BanUserRequest;
import gamehub.game_Hub.Request.SuspendAccountRequest;
import gamehub.game_Hub.Response.Admin.AccountStatusResponse;
import gamehub.game_Hub.Response.Admin.AdminReportsResponse;
import gamehub.game_Hub.Response.Admin.AdminSuspendedAccountsResponse;
import gamehub.game_Hub.Response.Admin.AdminUserResponse;
import gamehub.game_Hub.Response.Admin.ReportStatusResponse;
import gamehub.game_Hub.Response.Admin.RoleResponse;
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
import gamehub.game_Hub.enums.SuspensionStatus;
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

  private final CommunityGuidelinesRepository communityGuidelinesRepository;

  private final BanHistoryRepository banHistoryRepository;

  private final EmailService emailService;

  private final UserSuspensionRepository userSuspensionRepository;

  private final UserSuspensionsMapper userSuspensionsMapper;

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

    CommunityGuidelines banReason = communityGuidelinesRepository.findById(banUserRequest.getBanReason())
        .orElseThrow(() -> new EntityNotFoundException("No reason found with id: " + banUserRequest.getBanReason()));

    var banUser = BanHistory.builder()
        .user(user)
        .reason(banReason)
        .customMsg(banUserRequest.getCustomMessage())
        .build();

    user.setBanned(true);
    user.setAccountStatus(AccountStatus.BANNED);
    banHistoryRepository.save(banUser);

    // TODO GH-200 create method to change status for other reports related to user

    sendBannedUserEmail(user, banUserRequest.getCustomMessage(), banReason.getCommunityGuideline(),
        banReason.getDescription());
    return userRepository.save(user).getId();
  }

  @Override
  public Long unBanUser(final Long userId) throws MessagingException {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));

    user.setBanned(false);
    user.setAccountStatus(AccountStatus.ACTIVE);
    sendAccountRestoredEmail(user);
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
    return Arrays.stream(ReportStatus.values()).map(status -> new ReportStatusResponse(status.name())).toList();
  }

  @Override
  public Long changeStatusInReview(final Long reportId) {
    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new EntityNotFoundException("report with id: " + reportId + " was not found"));

    report.setStatus(ReportStatus.IN_REVIEW);
    return reportRepository.save(report).getId();
  }

  @Override
  public PageResponse<AdminSuspendedAccountsResponse> getAllSuspendedAccounts(final int page, final int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    Page<UserSuspensions> userSuspension = userSuspensionRepository.findOnePerUser(pageable);
    List<AdminSuspendedAccountsResponse> suspendedAccountsResponse = userSuspension.stream()
        .map(userSuspensionsMapper::toUserSuspensionResponse)
        .toList();

    return new PageResponse<>(
        suspendedAccountsResponse,
        userSuspension.getNumber(),
        userSuspension.getSize(),
        userSuspension.getTotalElements(),
        userSuspension.getTotalPages(),
        userSuspension.isFirst(),
        userSuspension.isLast()
    );
  }

  @Override
  public Long suspendAccount(final Long userId, final SuspendAccountRequest suspendAccountRequest)
      throws MessagingException {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));

    CommunityGuidelines suspendedReason = communityGuidelinesRepository.findById(
            suspendAccountRequest.getSuspendReason())
        .orElseThrow(
            () -> new EntityNotFoundException("No reason found with id: " + suspendAccountRequest.getSuspendReason()));

    Report report = reportRepository.findById(suspendAccountRequest.getReportId())
        .orElseThrow(() -> new EntityNotFoundException(
            "Report with with id: " + suspendAccountRequest.getReportId() + " was not found"));

    boolean isExpiresAtCustom = isExpiresAtCustom(suspendAccountRequest.getExpiresAt());

    var suspended = UserSuspensions.builder()
        .userId(user)
        .suspensionReason(suspendedReason)
        .customMessage(suspendAccountRequest.getCustomMessage())
        .report(report)
        .suspensionStatus(SuspensionStatus.ONGOING)
        .build();

    if (isExpiresAtCustom) {
      LocalDate customExpiresAt = LocalDate.parse(suspendAccountRequest.getExpiresAt());
      suspended.setExpiresAt(customExpiresAt.atStartOfDay());
    } else {
      Long plusDays = Long.parseLong(suspendAccountRequest.getExpiresAt());
      LocalDateTime expiresAt = LocalDate.now().plusDays(plusDays).atStartOfDay();
      suspended.setExpiresAt(expiresAt);
    }

    user.setAccountStatus(AccountStatus.SUSPENDED);
    report.setStatus(ReportStatus.RESOLVED);
    userRepository.save(user);

    // TODO GH-200 create method to change status for other reports related to user

    userSuspensionRepository.save(suspended);
    String violatedGuideline = suspendedReason.getCommunityGuideline();
    String customMsg = suspendAccountRequest.getCustomMessage();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    sendSuspendedAccountEmail(user, violatedGuideline, customMsg, suspended.getExpiresAt().format(formatter));
    return suspended.getId();

  }

  private boolean isExpiresAtCustom(String date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    try {
      dateFormat.parse(date);
    } catch (ParseException e) {
      return false;
    }
    return true;
  }

  private void sendSuspendedAccountEmail(final User user, final String violatedGuideline, final String customMsg,
      final String suspensionEndDate) throws MessagingException {
    emailService.sendSuspendedAccountEmail(user.getEmail(), user.getName(), violatedGuideline, customMsg,
        suspensionEndDate, EmailTemplate.USER_SUSPENDED_EMAIL, "Your GameHub has been suspended");
  }

  private void sendBannedUserEmail(final User user, final String customMsg, String banReason, String description)
      throws MessagingException {
    String reason = banHistoryRepository.findByUserId(user.getId())
        .stream()
        .findFirst()
        .map(banHistory -> banHistory.getReason().getCommunityGuideline())
        .orElse(null);

    // TODO dont send user.getId() but send id of ban when implementing chat between user and admin
    // on fe show report id with # report.getId()
    String appealUrl = "http://localhost:4200/send-appeal?appeal=" + user.getId();
    emailService.sendBannedUserEmail(user.getEmail(), user.getName(), banReason, customMsg, description,
        EmailTemplate.USER_BANNED_EMAIL, appealUrl, "Your GameHub account has been banned — Appeal available");
  }

  private void sendAccountRestoredEmail(final User user) throws MessagingException {
    emailService.sendAccountRestored(user.getEmail(), user.getName(), EmailTemplate.USER_ACCOUNT_RESTORED_EMAIL,
        logInUrl, "Your GameHub account has been successfully restored");
  }

}
